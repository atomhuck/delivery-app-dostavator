package com.example.dostavator.network

import com.google.gson.annotations.SerializedName

// Поля должны строго совпадать с LoginRequest на сервере
data class RemoteLoginRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String
)

// Поля должны строго совпадать с LoginResponse на сервере
data class RemoteLoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("token") val token: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("message") val message: String? = null // Важно для вывода ошибок
)