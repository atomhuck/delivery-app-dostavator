package com.example

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "jdbc:postgresql://ep-floral-sea-am52hw4z.c-5.us-east-1.aws.neon.tech:5432/neondb?sslmode=require"
            username = "neondb_owner"
            password = "npg_UpeGC1wNW0rF"

            maximumPoolSize = 10
            isAutoCommit = false
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        println("✅ Подключение к Neon PostgreSQL...")

        transaction {
            SchemaUtils.createMissingTablesAndColumns(Couriers, Zones, Restaurants, Orders)
        }

        println("✅ Таблицы проверены/созданы успешно!")
    }
}