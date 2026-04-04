package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.insert
import java.time.Instant

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080   // Railway передаёт порт сюда

    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        DatabaseFactory.init()

        // Создаём тестового курьера
        transaction {
            if (Couriers.select { Couriers.login eq "test" }.singleOrNull() == null) {
                Couriers.insert {
                    it[login] = "test"
                    it[passwordHash] = "123"
                    it[fullName] = "Иван Иванов"
                    it[isOnShift] = true
                    it[zoneId] = null
                    it[updatedAt] = Instant.now()
                }
                println("✅ Тестовый курьер создан")
            }
        }

        routing {
            get("/") {
                call.respondText("✅ Сервер на Railway работает!", ContentType.Text.Plain)
            }

            post("/api/auth/login") {
                call.respondText("""{"success":true,"token":"fake-token","name":"Иван Иванов"}""", ContentType.Application.Json)
            }

            get("/api/courier/me") {
                call.respondText(
                    """{"earnedToday":2450,"onShift":true,"name":"Иван Иванов"}""",
                    ContentType.Application.Json
                )
            }
        }
    }.start(wait = true)
}