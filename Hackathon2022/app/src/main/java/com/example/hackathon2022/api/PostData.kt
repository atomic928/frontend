package com.example.hackathon2022.api

import com.squareup.moshi.Json

class PostData {
    data class RegisterData(
        @Json(name = "username") val username: String,
        @Json(name = "password") val password: String
    )

    data class LoginData(
        @Json(name = "username") val username: String,
        @Json(name = "password") val password: String
    )

    data class DriveLogData(
        @Json(name = "token") val token: String,
        @Json(name = "date") val date: String,
        @Json(name = "speed") val speed: Float,
        @Json(name = "acceleration") val acceleration: Float,
        @Json(name = "latitude") val latitude: Float,
        @Json(name = "longitude") val longitude: Float
    )
}