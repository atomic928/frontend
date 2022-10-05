package com.example.hackathon2022

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hackathon2022.databinding.ActivityMainBinding
import com.example.hackathon2022.ui.home.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), SensorEventListener, LocationListener {

    private var mManager: SensorManager by Delegates.notNull()
    private var mSensor: Sensor by Delegates.notNull()
    private lateinit var locationManager: LocationManager
    private var speed = 0f

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            if (nd.id == R.id.navigation_login || nd.id == R.id.navigation_signup) {
                navView.visibility = View.GONE
            } else{
                navView.visibility = View.VISIBLE
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    //センサーに何かしらのイベントが発生したときに呼ばれる
    override fun onSensorChanged(event: SensorEvent?) {
        //3つの値が配列で入ってくる
        Log.v("sensorTest", "______")
        //X軸方法
        Log.v("sensorTest", event?.values!![0].toString())
        //Y軸方法
        Log.v("sensorTest", event.values!![1].toString())
        //Z軸方法
        Log.v("sensorTest", event.values!![2].toString())

        homeViewModel.putAcceleration(event.values!!)

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onPause() {
        super.onPause()
        mManager.unregisterListener(this)
        //ポーズ時にはGPS（位置情報）の取得を解除する
        locationManager.removeUpdates(this)
    }

    override fun onResume() {
        super.onResume()
        
        mManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onLocationChanged(location: Location) {
        speed = if (location.hasSpeed()) {
            //速度が出ている時(km/hに変換して変数speedへ)
            location.speed * 3.6f
        } else {
            //速度が出ていない時
            0f
        }
        homeViewModel.putSpeed(speed)
    }
}

