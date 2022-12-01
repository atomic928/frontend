package com.example.hackathon2022

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hackathon2022.databinding.ActivityMainBinding
import com.example.hackathon2022.model.Date
import com.example.hackathon2022.model.DateRoomDatabase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), SensorEventListener, LocationListener {

    private var mManager: SensorManager by Delegates.notNull()
    private var mSensor: Sensor by Delegates.notNull()
    private lateinit var locationManager: LocationManager
    private var speed = 0f
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    private val sensorViewModel: SensorViewModel by viewModels {
        SensorViewModelFactory((application as DateApplication).repository)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ダークモードかどうかを取得する
        val sharedPref = getSharedPreferences("Dark", Context.MODE_PRIVATE)

        val isDark = sharedPref.getString("isDark", "False")

        if (isDark == "True") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        //センサーマネージャーを取得する
        mManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //加速度計のセンサーを取得する
        //その他のセンサーを取得する場合には引数を違うものに変更する
        mSensor = mManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        //ロケーションマネージャーに端末のロケーションサービスを関連づける
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_login, R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.navigation_login || nd.id == R.id.navigation_signup || nd.id == R.id.navigation_map) {
                navView.visibility = View.GONE
            } else{
                navView.visibility = View.VISIBLE
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    //センサーに何かしらのイベントが発生したときに呼ばれる
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        // 加速度を送る
        sensorViewModel.putAcceleration(event?.values!!)

        // 加速度の大きさが一定値を超えた時に位置を記録する（運転中の時のみ）
        if (speed > 30) {
            val magnitudeOfAcceleration = sqrt(event.values[0].pow(2)+event.values[1].pow(2)+event.values[2].pow(2))
            if (magnitudeOfAcceleration >= 7.9) {
                val dateNow = LocalDate.now().toString()+ "-" + LocalTime.now().toString()

                val URL = "https://maps.googleapis.com/maps/api/staticmap?center=$latitude,$longitude&size=640x320&scale=1&zoom=18&key=AIzaSyA-cfLegBoleKaT2TbU5R4K1uRkzBR6vUQ&markers=color:red|$latitude,$longitude"
                val date = Date(0, dateNow, URL)

                sensorViewModel.insert(date)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onPause() {
        super.onPause()
        mManager.unregisterListener(this)
        //ポーズ時にはGPSの取得を解除する
        locationManager.removeUpdates(this)
    }

    override fun onResume() {
        super.onResume()
        //GPSの取得許可があるのかをチェック
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //GPSの使用が許可されていなければパーミッションを要求し、その後再度チェックが行われる
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        mManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onLocationChanged(location: Location) {
        // 速度の取得
        speed = if (location.hasSpeed()) {
            location.speed*3.6f
        } else {
            0f
        }
        sensorViewModel.putSpeed(speed)
        // 現在地取得
        latitude = location.latitude
        longitude = location.longitude
    }
}


