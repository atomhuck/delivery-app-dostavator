package com.example.dostavator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dostavator.ui.screens.*
import com.example.dostavator.viewmodel.ShiftViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val shiftViewModel: ShiftViewModel = viewModel()

            val startDestination = if (shiftViewModel.isLoggedIn) "main" else "auth"

            NavHost(navController = navController, startDestination = startDestination) {
                // 1. Авторизация
                composable("auth") {
                    AuthScreen(onLoginSuccess = {
                        shiftViewModel.login()
                        navController.navigate("main") {
                            popUpTo("auth") { inclusive = true }
                        }
                    })
                }

                // 2. Главный экран (Где кнопка "Выйти на смену")
                composable("main") {
                    MainScreen(navController, shiftViewModel)
                }

                // 3. ИСПРАВЛЕНО: Добавлен экран активной смены (Список заказов)
                // Без этого блока вылетало при нажатии "Выйти на смену"
                composable("active_shift") {
                    ActiveShiftScreen(navController, shiftViewModel)
                }

                // 4. Профиль (Настройки)
                composable("profile") {
                    ProfileScreen(navController, shiftViewModel)
                }

                // 5. Архив (Отдельный экран истории)
                composable("archive") {
                    ArchiveScreen(navController, shiftViewModel)
                }

                // 6. Детали заказа
                composable("order_details") {
                    OrderDetailsScreen(navController, shiftViewModel)
                }
            }
        }
    }
}