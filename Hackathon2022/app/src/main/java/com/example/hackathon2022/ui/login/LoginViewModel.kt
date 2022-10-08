package com.example.hackathon2022.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hackathon2022.api.ResponseData
import com.hadilq.liveevent.LiveEvent
import retrofit2.Response

class LoginViewModel: ViewModel() {
    //    private val apiRepository = ApiRepository.instance

    private val _postResponse = LiveEvent<Response<ResponseData.ResponseGetUser>>()
    val postResponse: LiveData<Response<ResponseData.ResponseGetUser>> get() = _postResponse


    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

//    fun login(name: String, pass: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val postData = PostData.RegisterData(name, pass)
//                val response = apiRepository.postLogin(postData)
//                _postResponse.postValue(response)
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
}