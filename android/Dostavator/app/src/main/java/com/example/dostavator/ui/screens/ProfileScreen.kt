package com.example.dostavator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dostavator.ui.components.BottomNavigationBar
import com.example.dostavator.viewmodel.ShiftViewModel

@Composable
fun ProfileScreen(navController: NavController, viewModel: ShiftViewModel) {
    val purplePrimary = Color(0xFF9139BA)

    Scaffold(
        bottomBar = {
            // Передаем индекс 1, так как это вторая вкладка
            BottomNavigationBar(
                selectedTab = 1,
                onTabSelected = { index ->
                    if (index == 0) navController.navigate("main")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFB))
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Иконка профиля (заглушка)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Имя Курьера",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Статус курьера, который берется из общей ViewModel
            Surface(
                color = if (viewModel.isOnShift) Color(0xFFDCFCE7) else Color(0xFFF3F4F6),
                shape = RoundedCornerShape(99.dp)
            ) {
                Text(
                    text = if (viewModel.isOnShift) "Сейчас на смене" else "Отдыхает",
                    color = if (viewModel.isOnShift) Color(0xFF166534) else Color.Gray,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Карточка со статистикой
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Статистика за сегодня",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF3F4F6))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Заработано:", color = Color.Gray)
                        Text(
                            text = "${viewModel.todayEarnings} ₽",
                            fontWeight = FontWeight.Bold,
                            color = purplePrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка выхода из аккаунта
            OutlinedButton(
                onClick = {
                    viewModel.stopShift() // На всякий случай останавливаем смену
                    navController.navigate("auth") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text("ВЫЙТИ ИЗ АККАУНТА", fontWeight = FontWeight.Bold)
            }
        }
    }
}