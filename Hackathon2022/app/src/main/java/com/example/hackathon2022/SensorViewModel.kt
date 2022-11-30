package com.example.hackathon2022

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SensorViewModel: ViewModel() {
    private val _acceleration = MutableLiveData<FloatArray>()
    val acceleration: LiveData<FloatArray> = _acceleration

    fun putAcceleration(p0: FloatArray) {
        _acceleration.value = p0
    }

    private val _speed = MutableLiveData<Float>()
    val speed: LiveData<Float> = _speed

    fun putSpeed(p0: Float) {
        _speed.value = p0
    }

    private val _backgroundColor = MutableLiveData<Int>()
    val backgroundColor: LiveData<Int> = _backgroundColor

    fun putBackgroundColor(color: Int) {
        _backgroundColor.value = color
    }

    private val _map = MutableLiveData<Bitmap>()
    val map: LiveData<Bitmap> = _map

    fun putMap(p0: Bitmap) {
        _map.value = p0
    }

    private val _dateList = MutableLiveData<List<String>>()
    val dateList: LiveData<List<String>> = _dateList

    var dataListSize = 0

    fun putDateList(p0: List<String>) {
        _dateList.value = p0
    }
}