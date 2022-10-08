package com.example.hackathon2022.ui.singup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackathon2022.api.PostData
import com.example.hackathon2022.api.ResponseData
import com.example.hackathon2022.repository.ApiRepository
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class SignupViewModel : ViewModel() {
    private val apiRepository = ApiRepository.instance

    private val _postResponse = LiveEvent<Response<ResponseData.ResponseGetUser>>()
    val postResponse: LiveData<Response<ResponseData.ResponseGetUser>> get() = _postResponse


    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    fun registerUser(name: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val postData = PostData.RegisterData(name, pass)
                val response = apiRepository.postRegister(postData)
                _postResponse.postValue(response)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
}