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

            // Проверяем статус при запуске: если залогинен — идем в main, если нет — в auth
            val startDestination = if (shiftViewModel.isLoggedIn) "main" else "auth"

            NavHost(navController = navController, startDestination = startDestination) {
                composable("auth") {
                    AuthScreen(onLoginSuccess = {
                        shiftViewModel.login() // Сохраняем вход в SharedPreferences
                        navController.navigate("main") {
                            popUpTo("auth") { inclusive = true }
                        }
                    })
                }
                composable("main") { MainScreen(navController, shiftViewModel) }
                composable("active_shift") { ActiveShiftScreen(navController, shiftViewModel) }
                composable("profile") { ProfileScreen(navController, shiftViewModel) }
            }
        }
    }
}