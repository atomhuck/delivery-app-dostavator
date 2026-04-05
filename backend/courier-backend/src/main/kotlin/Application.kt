package com.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

// Модель запроса от Android
@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)

// НОВАЯ МОДЕЛЬ: Исправляет ошибку "Serializing collections of different element types"
@Serializable
data class LoginResponse(
    val success: Boolean,
    val token: String? = null,
    val name: String? = null,
    val message: String? = null
)

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) {
            json()
        }

        DatabaseFactory.init()

        transaction {
            SchemaUtils.create(Couriers)
            if (Couriers.select { Couriers.login eq "test" }.singleOrNull() == null) {
                Couriers.insert {
                    it[login] = "test"
                    it[passwordHash] = "123"
                    it[fullName] = "Иван Иванов"
                    it[isOnShift] = true
                    it[updatedAt] = Instant.now()
                    it[refferalLink] = "https://link.com"
                }
            }
        }

        configureRouting()
    }.start(wait = true)
}