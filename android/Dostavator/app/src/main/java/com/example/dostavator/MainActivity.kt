package com.example.dostavator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dostavator.ui.screens.*
import com.example.dostavator.viewmodel.ShiftViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val shiftViewModel: ShiftViewModel = viewModel()

            NavHost(
                navController = navController,
                startDestination = "auth"
            ) {
                composable("auth") {
                    AuthScreen(onLoginSuccess = {
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