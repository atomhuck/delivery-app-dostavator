# Dostavator — Курьерская доставка

Приложение для курьеров и управления доставкой еды/товаров.

## О проекте

**Dostavator** — это система доставки, состоящая из:
- **Android-приложения** для курьеров
- **Backend-сервера** (Ktor + PostgreSQL)

## Технологии

### Backend (сервер)
- **Ktor** — фреймворк для веб-сервера
- **Kotlin** + **Exposed** — работа с базой данных
- **PostgreSQL** (Neon)
- **HikariCP** — пул соединений

### Android
- Kotlin
- jetpack compose
- ui

Для связи бэка и фронта использовался Retrofit2 

## Как запустить проект локально

### 1. Backend (сервер)

bash
cd backend/courier-backend

### Запуск через IntelliJ
Открой проект и запусти Application.kt

### Или через терминал
./gradlew run

Сервер будет доступен по адресу: http://localhost:8080
Тестовый аккаунт курьера:

Login: test
Password: 123

### 2. Android-приложение
Открой папку android/Dostavator в Android Studio и запусти приложение.

Команда

Android — @atomhuck
Backend — @Seferaki 
Designer — @sarattova
Data-engenier — @asemenov2004
