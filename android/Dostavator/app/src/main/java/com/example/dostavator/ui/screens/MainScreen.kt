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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dostavator.R
import com.example.dostavator.ui.components.BottomNavigationBar
import com.example.dostavator.viewmodel.ShiftViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: ShiftViewModel) {
    val purplePrimary = Color(0xFF9139BA)

    // Если курьер уже нажал "На смену", при открытии приложения сразу летим к поиску
    LaunchedEffect(viewModel.isOnShift) {
        if (viewModel.isOnShift) {
            navController.navigate("active_shift")
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = 0,
                onTabSelected = { index ->
                    if (index == 1) navController.navigate("profile")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 1. ВЕРНУЛИ ВЕРХНЮЮ ПЛАШКУ ЗАРАБОТКА
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .background(Color(0xFFF0E3F7), shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.Gray)) { append("ЗАРАБОТАНО СЕГОДНЯ: ") }
                        withStyle(SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
                            append("${viewModel.todayEarnings} ₽")
                        }
                    },
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Картинка курьера
            Image(
                painter = painterResource(id = R.drawable.tired_courier),
                contentDescription = null,
                modifier = Modifier.size(260.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.weight(1f))

            // Кнопка выхода на смену
            Button(
                onClick = {
                    viewModel.startShift()
                    navController.navigate("active_shift")
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = purplePrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("ВЫЙТИ НА СМЕНУ", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. ВЕРНУЛИ КНОПКУ ПРИГЛАСИТЬ ДРУГА
            Button(
                onClick = {
                    // Здесь будет функционал шеринга или копирования ссылки
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = purplePrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("ПРИГЛАСИТЬ ДРУГА", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}