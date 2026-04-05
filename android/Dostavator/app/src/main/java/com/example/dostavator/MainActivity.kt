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
import com.example.dostavator.viewmodel.AuthViewModel
import com.example.dostavator.viewmodel.ShiftViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val shiftViewModel: ShiftViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel()

            // Автоматический выбор стартового экрана
            val startDestination = if (shiftViewModel.isLoggedIn) "main" else "auth"

            NavHost(navController = navController, startDestination = startDestination) {

                // 1. Авторизация (связана с Ktor через AuthViewModel)
                composable("auth") {
                    AuthScreen(navController = navController, viewModel = authViewModel)
                }

                // 2. Главный экран
                composable("main") {
                    MainScreen(navController, shiftViewModel)
                }

                // 3. Активная смена (список доступных заказов)
                composable("active_shift") {
                    ActiveShiftScreen(navController, shiftViewModel)
                }

                // 4. Профиль
                composable("profile") {
                    ProfileScreen(navController, shiftViewModel)
                }

                // 5. Архив (история выполненных заказов)
                composable("archive") {
                    ArchiveScreen(navController, shiftViewModel)
                }

                // 6. Детали заказа (экран со свайпами "Забрал/Отдал")
                composable("order_details") {
                    OrderDetailsScreen(navController, shiftViewModel)
                }
            }
        }
    }
}