package com.example.hackathon2022.ui.dashboard

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _map = MutableLiveData<Bitmap>()
    val map: LiveData<Bitmap> = _map

    fun putMap(p0: Bitmap) {
        _map.value = p0
    }
}