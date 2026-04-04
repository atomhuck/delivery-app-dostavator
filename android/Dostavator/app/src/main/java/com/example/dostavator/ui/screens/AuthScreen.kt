package com.example.dostavator.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(onLoginSuccess: () -> Unit) {
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Текст ошибки
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val purplePrimary = Color(0xFF9139BA)
    val purpleLight = Color(0xFFDBC0E8)
    val blackText = Color(0xFF000000)
    val blackText35 = Color(0x59000000)
    val errorColor = Color(0xFFD32F2F)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val adaptiveRadius = size.width * 1.2f

            drawCircle(
                color = Color(0xFFDBC0E8).copy(alpha = 0.71f),
                radius = adaptiveRadius,
                center = Offset(
                    x = size.width / 2,
                    y = -adaptiveRadius + (size.height * 0.30f)
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Логотип Доставатор (Жирный)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Доста", fontSize = 30.sp, fontWeight = FontWeight.Black, color = blackText)
                Text(text = "ватор", fontSize = 30.sp, fontWeight = FontWeight.Black, color = purplePrimary)
            }

            Text(text = "PRO", fontSize = 30.sp, fontWeight = FontWeight.Black, color = blackText)

            Spacer(modifier = Modifier.height(40.dp))

            // Вход (Жирный)
            Text(text = "Вход", fontSize = 25.sp, fontWeight = FontWeight.Black, color = blackText)

            Spacer(modifier = Modifier.height(40.dp))

            // Поле ID
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "ID", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = blackText)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = id,
                    onValueChange = {
                        id = it
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    placeholder = {
                        Text(text = "Введите ID", color = blackText35, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = purplePrimary,
                        unfocusedBorderColor = purplePrimary
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Поле Password
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Password", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = blackText)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    placeholder = {
                        Text(text = "Введите пароль", color = blackText35, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    },
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = purplePrimary.copy(alpha = 0.35f)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = purplePrimary,
                        unfocusedBorderColor = purplePrimary
                    ),
                    singleLine = true
                )
            }

            // Динамический вывод ошибки
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp), // Фиксированная высота, чтобы кнопка не прыгала
                contentAlignment = Alignment.Center
            ) {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = errorColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Кнопка ВХОД
            Button(
                onClick = {
                    when {
                        id.isBlank() -> errorMessage = "Введите ID"
                        password.isBlank() -> errorMessage = "Введите пароль"
                        id == "123" && password == "123" -> {
                            errorMessage = null
                            onLoginSuccess()
                        }
                        else -> errorMessage = "Неверный ID или пароль"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .shadow(8.dp, RoundedCornerShape(8.dp), ambientColor = purplePrimary, spotColor = purplePrimary),
                colors = ButtonDefaults.buttonColors(containerColor = purplePrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "ВХОД", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}