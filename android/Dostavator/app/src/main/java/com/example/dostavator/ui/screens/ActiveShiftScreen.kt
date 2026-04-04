package com.example.dostavator.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dostavator.ui.components.BottomNavigationBar
import com.example.dostavator.viewmodel.ShiftViewModel

@Composable
fun ActiveShiftScreen(navController: NavController, viewModel: ShiftViewModel) {
    val purplePrimary = Color(0xFF9139BA)
    val grayText = Color(0xFF9CA3AF)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedTab = 0, onTabSelected = { index ->
                if (index == 1) navController.navigate("profile")
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFB))
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ВЕРНУЛИ ПЛАШКУ СЮДА
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(
                        color = purplePrimary,
                        shape = RoundedCornerShape(99.dp)
                    ) {
                        Text(
                            text = "НА СМЕНЕ",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "ЗАРАБОТАНО СЕГОДНЯ: ${viewModel.todayEarnings} ₽",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Поле поиска
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Поиск заказа", color = grayText, fontSize = 14.sp)
                        SlowJumpingDots(color = grayText)
                    }
                },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = grayText) },
                enabled = false,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    disabledBorderColor = Color(0xFFE5E7EB)
                )
            )

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Пока пусто",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Black,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.weight(1f))

            // Кнопка завершения
            Button(
                onClick = {
                    viewModel.stopShift()
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("ЗАВЕРШЕНИЕ", fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SlowJumpingDots(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    val dotAnimations = listOf(0, 1, 2).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f, targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1200
                    0f at 0 with LinearOutSlowInEasing
                    -4f at 300 with FastOutLinearInEasing
                    0f at 600 with FastOutLinearInEasing
                    0f at 1200
                },
                initialStartOffset = StartOffset(index * 200)
            ), label = ""
        )
    }
    Row(modifier = Modifier.padding(start = 3.dp, top = 2.dp), verticalAlignment = Alignment.Bottom) {
        dotAnimations.forEach { anim ->
            Box(Modifier.padding(horizontal = 1.5.dp).size(3.5.dp).offset(y = anim.value.dp).background(color, CircleShape))
        }
    }
}