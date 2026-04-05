package com.example.dostavator.network

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// ЭТИ КЛАССЫ ДОЛЖНЫ БЫТЬ ВНЕ ОБЪЕКТА NetworkManager
data class LoginRequest(
    @SerializedName("login") val login: String,
    @SerializedName("pass") val pass: String
)

data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("token") val token: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("message") val message: String? = null
)

interface DeliveryApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: RemoteLoginRequest): RemoteLoginResponse
}

object NetworkManager {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val api: DeliveryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeliveryApi::class.java)
    }
}