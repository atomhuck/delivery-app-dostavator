package com.example

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

// Курьеры
object Couriers : IntIdTable(name = "delivery.couriers") {
    val login = text("login").uniqueIndex()
    val passwordHash = text("password_hash")
    val fullName = text("full_name").nullable()
    val isOnShift = bool("is_on_shift").default(false)
    val zoneId = integer("zone_id").nullable()
    val updatedAt = timestamp("updated_at")
}

// Зоны
object Zones : IntIdTable(name = "delivery.zones") {
    val name = text("name").uniqueIndex()
}

// Рестораны
object Restaurants : IntIdTable(name = "delivery.restaurants") {
    val name = text("name")
    val address = text("address")
    val contactPhone = varchar("contact_phone", 20).nullable()
    val isActive = bool("is_active").default(true)
    val createdAt = timestamp("created_at")
}

// Заказы — упростили items_json до text
object Orders : IntIdTable(name = "delivery.orders") {
    val restaurantId = integer("restaurant_id").references(Restaurants.id)
    val courierId = integer("courier_id").references(Couriers.id).nullable()
    val zoneId = integer("zone_id").references(Zones.id)

    val status = varchar("status", 20).default("CREATED")

    val deliveryAddress = text("delivery_address")
    val totalPrice = decimal("total_price", 10, 2)
    val itemsJson = text("items_json").nullable()        // ← изменили на text

    val createdAt = timestamp("created_at")
    val acceptedAt = timestamp("accepted_at").nullable()
    val completedAt = timestamp("completed_at").nullable()
}