package com.example.dostavator.data.models

enum class CourierState {
    SHIFT_NOT_STARTED,    // Смена не начата
    SEARCHING_RESTAURANT, // Поиск ресторана
    ORDER_RECEIVED,       // Заказ получен
    ORDER_ACCEPTED,       // Заказ принят из ресторана
    ORDER_DELIVERED       // Заказ отдан
}

data class CourierStats(
    val todayDeliveries: Int = 0,
    val todayEarnings: Int = 0,
    val todayHours: Float = 0f
)

data class Order(
    val id: String = "#12345",
    val restaurantName: String = "Суши-Мастер",
    val restaurantAddress: String = "ул. Ленина, 15",
    val customerName: String = "Иван Петров",
    val customerPhone: String = "+7 (999) 123-45-67",
    val deliveryAddress: String = "ул. Пушкина, д. 10, кв. 5",
    val amount: Int = 1250,
    val estimatedTime: Int = 25
)