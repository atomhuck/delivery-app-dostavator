package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureRouting() {
    routing {
        post("/api/auth/login") {
            try {
                val request = call.receive<LoginRequest>()

                val courier = transaction {
                    Couriers.select { Couriers.login eq request.login }.singleOrNull()
                }

                if (courier != null && courier[Couriers.passwordHash] == request.password) {
                    // Используем объект LoginResponse вместо mapOf
                    call.respond(LoginResponse(
                        success = true,
                        token = "fake-jwt-token-123",
                        name = courier[Couriers.fullName] ?: "Курьер"
                    ))
                } else {
                    call.respond(HttpStatusCode.Unauthorized, LoginResponse(
                        success = false,
                        message = "Неверный логин или пароль"
                    ))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, LoginResponse(
                    success = false,
                    message = "Ошибка сервера: ${e.localizedMessage}"
                ))
            }
        }
    }
}