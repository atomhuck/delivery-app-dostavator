package com.example.dostavator.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel

data class OrderItem(
    val id: String,
    val address: String,
    val price: Int,
    val distance: Double
)

// Используем AndroidViewModel для доступа к SharedPreferences
class ShiftViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefs = application.getSharedPreferences("dostavator_prefs", Context.MODE_PRIVATE)

    var userName by mutableStateOf("Алексей")
    var paymentDetails by mutableStateOf("123456789")
    var profileImageUri by mutableStateOf<Uri?>(null)
    var todayEarnings by mutableStateOf(0)
    var isOnShift by mutableStateOf(false)

    // Храним статус логина в памяти
    var isLoggedIn by mutableStateOf(sharedPrefs.getBoolean("is_logged_in", false))
        private set

    private val _availableOrders = mutableStateListOf<OrderItem>()
    val availableOrders: List<OrderItem> get() = _availableOrders

    init {
        loadMockOrders()
    }

    fun login() {
        isLoggedIn = true
        sharedPrefs.edit().putBoolean("is_logged_in", true).apply()
    }

    fun logout() {
        isLoggedIn = false
        isOnShift = false
        todayEarnings = 0
        profileImageUri = null
        sharedPrefs.edit().putBoolean("is_logged_in", false).apply()
    }

    fun loadMockOrders() {
        _availableOrders.clear()
        _availableOrders.addAll(
            listOf(
                OrderItem("#1245", "ул. Ленина, 45", 550, 1.2),
                OrderItem("#1246", "пр. Мира, 128", 720, 2.1)
            )
        )
    }

    fun startShift() { isOnShift = true }
    fun stopShift() { isOnShift = false }

    fun acceptOrder(order: OrderItem) {
        todayEarnings += order.price
        _availableOrders.remove(order)
    }

    fun updatePaymentDetails(newDetails: String) { paymentDetails = newDetails }
    fun updateProfileImage(uri: Uri?) { profileImageUri = uri }
}