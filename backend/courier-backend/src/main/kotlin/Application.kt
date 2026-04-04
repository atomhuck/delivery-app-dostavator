package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        DatabaseFactory.init()

        // Создаём тестового курьера при первом запуске
        transaction {
            if (Couriers.select { Couriers.login eq "test" }.singleOrNull() == null) {
                Couriers.insert {
                    it[login] = "test"
                    it[passwordHash] = "123"
                    it[fullName] = "Иван Иванов"
                    it[isOnShift] = true
                    it[zoneId] = null
                    it[updatedAt] = Instant.now()
                    it[refferalLink] = "https://example.com/ref/test123"   // любое значение
                }
                println("✅ Создан тестовый курьер → login: test | password: 123")
            }
        }

        routing {

            get("/") {
                call.respondText("✅ Сервер курьера работает на http://localhost:8080", ContentType.Text.Plain)
            }

            // Авторизация (принимает JSON)
            post("/api/auth/login") {
                try {
                    val request = call.receive<LoginRequest>()

                    val courier = transaction {
                        Couriers.select { Couriers.login eq request.login }.singleOrNull()
                    }

                    if (courier != null && courier[Couriers.passwordHash] == request.password) {
                        call.respondText(
                            """{
                                "success": true,
                                "token": "fake-jwt-token-${courier[Couriers.id].value}",
                                "courierId": ${courier[Couriers.id].value},
                                "name": "${courier[Couriers.fullName] ?: "Курьер"}",
                                "isOnShift": ${courier[Couriers.isOnShift]}
                            }""".trimIndent(),
                            ContentType.Application.Json
                        )
                    } else {
                        call.respondText(
                            """{"success": false, "message": "Неверный логин или пароль"}""",
                            ContentType.Application.Json
                        )
                    }
                } catch (e: Exception) {
                    call.respondText(
                        """{"success": false, "message": "Ошибка чтения данных"}""",
                        ContentType.Application.Json
                    )
                }
            }

            // Главная страница
            get("/api/courier/me") {
                val data = transaction {
                    Couriers.select { Couriers.login eq "test" }.singleOrNull()
                }

                if (data != null) {
                    call.respondText(
                        """{
                            "earnedToday": 2450,
                            "onShift": ${data[Couriers.isOnShift]},
                            "name": "${data[Couriers.fullName] ?: "Иван Иванов"}",
                            "message": "Заработано сегодня"
                        }""".trimIndent(),
                        ContentType.Application.Json
                    )
                } else {
                    call.respondText("""{"error": "Курьер не найден"}""", ContentType.Application.Json)
                }
            }

            // Заглушка для заказов
            get("/api/orders/available") {
                call.respondText("[]", ContentType.Application.Json)
            }
        }
    }.start(wait = true)
}