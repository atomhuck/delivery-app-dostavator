package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        get("/") {
            call.respondText("✅ Сервер работает! Готов к хакатону", ContentType.Text.Plain)
        }

        // === Очень простой вариант без сложных строк ===
        get("/api/courier/me") {
            call.respondText("""{"earnedToday":2450,"onShift":true,"message":"Заработано сегодня"}""", ContentType.Application.Json)
        }

        get("/api/orders/available") {
            call.respondText("""[{"id":"1093-293","restaurant":"Los Pollos & More","cost":300,"distance":1.2,"time":"21:15"},{"id":"1093-294","restaurant":"Суши Wok","cost":450,"distance":0.8,"time":"21:20"}]""", ContentType.Application.Json)
        }

        post("/api/auth/login") {
            call.respondText("""{"token":"fake-jwt-token-12345","courierId":"1","name":"Иван Иванов"}""", ContentType.Application.Json)
        }

        get("/api/orders/{id}") {
            val id = call.parameters["id"] ?: "unknown"
            call.respondText("""{"id":"$id","restaurant":"Los Pollos & More","cost":300,"distance":1.2}""", ContentType.Application.Json)
        }

        post("/api/courier/shift") {
            call.respondText("""{"status":"ok","onShift":true}""", ContentType.Application.Json)
        }

        post("/api/orders/{id}/accept") {
            val id = call.parameters["id"] ?: ""
            call.respondText("""{"status":"ok","message":"Заказ $id принят"}""", ContentType.Application.Json)
        }
    }
}