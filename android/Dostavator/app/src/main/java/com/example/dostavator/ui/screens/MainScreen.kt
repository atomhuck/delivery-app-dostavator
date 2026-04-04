package com.example.dostavator.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dostavator.R
import com.example.dostavator.ui.components.BottomNavigationBar
import com.example.dostavator.viewmodel.ShiftViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: ShiftViewModel) {
    // Проверка: если курьер уже на смене, перекидываем его на экран поиска сразу
    LaunchedEffect(viewModel.isOnShift) {
        if (viewModel.isOnShift) {
            navController.navigate("active_shift")
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedTab = 0, onTabSelected = { index ->
                if (index == 1) navController.navigate("profile")
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(padding).padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            Image(painter = painterResource(id = R.drawable.tired_courier), contentDescription = null, modifier = Modifier.size(260.dp))
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.startShift() // Активируем статус
                    navController.navigate("active_shift")
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9139BA)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("ВЫЙТИ НА СМЕНУ", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}