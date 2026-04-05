package com.example.dostavator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dostavator.network.NetworkManager
import com.example.dostavator.network.RemoteLoginRequest // Проверь этот импорт!
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Состояния экрана авторизации
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val token: String, val name: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(login: String, pass: String) {
        // Валидация пустых полей
        if (login.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Введите логин и пароль")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                // 1. Создаем запрос. Используем RemoteLoginRequest.
                // ВАЖНО: поле в модели называется 'password', а мы передаем туда значение 'pass'
                val loginRequest = RemoteLoginRequest(
                    login = login,
                    password = pass
                )

                // 2. Отправляем запрос через Retrofit
                val response = NetworkManager.api.login(loginRequest)

                // 3. Обрабатываем ответ от сервера
                if (response.success) {
                    _authState.value = AuthState.Success(
                        token = response.token ?: "",
                        name = response.name ?: "Курьер"
                    )
                } else {
                    // Сервер прислал ошибку (например, неверный пароль)
                    _authState.value = AuthState.Error(response.message ?: "Ошибка авторизации")
                }

            } catch (e: Exception) {
                // Ошибка сети или ошибка 500 от сервера
                val errorMsg = e.localizedMessage ?: "Неизвестная ошибка"
                _authState.value = AuthState.Error("Ошибка подключения: $errorMsg")
                e.printStackTrace()
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}