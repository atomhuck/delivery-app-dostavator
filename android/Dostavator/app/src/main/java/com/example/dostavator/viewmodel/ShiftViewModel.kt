package com.example.dostavator.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel

// Исправлено: price теперь Int, distance — Double (как в твоем оригинале)
data class OrderItem(
    val id: String,
    val address: String,
    val price: Int,
    val distance: Double,
    val items: List<String> = listOf("Пицца Маргарита x1", "Кола 0.5л x1")
)

class ShiftViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefs = application.getSharedPreferences("dostavator_prefs", Context.MODE_PRIVATE)

    var userName by mutableStateOf("Алексей")
    var paymentDetails by mutableStateOf("123456789")
    var profileImageUri by mutableStateOf<Uri?>(null)
    var todayEarnings by mutableStateOf(0)
    var isOnShift by mutableStateOf(false)

    // Состояние логина из SharedPreferences
    var isLoggedIn by mutableStateOf(sharedPrefs.getBoolean("is_logged_in", false))
        private set

    private val _availableOrders = mutableStateListOf<OrderItem>()
    val availableOrders: List<OrderItem> get() = _availableOrders

    // Текущий заказ и АРХИВ (Completed)
    var currentActiveOrder by mutableStateOf<OrderItem?>(null)
    val completedOrders = mutableStateListOf<OrderItem>()

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
        currentActiveOrder = null
        completedOrders.clear()
        sharedPrefs.edit().putBoolean("is_logged_in", false).apply()
    }

    fun loadMockOrders() {
        _availableOrders.clear()
        _availableOrders.addAll(
            listOf(
                OrderItem("#1245", "ул. Ленина, 45", 550, 1.2),
                OrderItem("#1246", "пр. Мира, 128", 720, 2.1),
                OrderItem("#1247", "ул. Гагарина, 8", 430, 0.8)
            )
        )
    }

    fun startShift() { isOnShift = true }
    fun stopShift() { isOnShift = false }

    fun acceptOrder(order: OrderItem) {
        currentActiveOrder = order
        _availableOrders.remove(order)
    }

    fun completeOrder() {
        currentActiveOrder?.let { order ->
            // 1. Начисляем доход (можно брать фиксированные 150 или процент от заказа)
            todayEarnings += 150

            // 2. Добавляем в список выполненных (Архив)
            if (!completedOrders.contains(order)) {
                completedOrders.add(order)
            }

            // 3. Сбрасываем активный заказ
            currentActiveOrder = null
        }
    }

    fun updatePaymentDetails(newDetails: String) { paymentDetails = newDetails }
    fun updateProfileImage(uri: Uri?) { profileImageUri = uri }
}