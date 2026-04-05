package com.example.dostavator.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dostavator.viewmodel.ShiftViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(navController: NavController, viewModel: ShiftViewModel) {
    // Берем текущий активный заказ из ViewModel
    val order = viewModel.currentActiveOrder ?: return
    var isPickedUp by remember { mutableStateOf(false) }

    val purpleGradient = Brush.horizontalGradient(
        listOf(Color(0xFFA357C6), Color(0xFF9139BA))
    )

    // Блокируем системную кнопку "Назад", пока заказ в работе
    BackHandler(enabled = true) { /* Выход запрещен во время заказа */ }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Заказ №${order.id}", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFB))
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 260.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ШАГ 1: ТОЧКА СБОРА (Ресторан) - Данные из объекта order
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(48.dp),
                                color = Color(0xFFFFEDD5),
                                shape = CircleShape
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🏠", fontSize = 20.sp)
                                }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(text = order.restaurant, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(text = "Точка сбора заказа", color = Color.Gray, fontSize = 12.sp)
                                Text(text = "📍 ${order.distance} км", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                }

                // ШАГ 2: ДАННЫЕ КЛИЕНТА - Появляются только после свайпа "Забрал заказ"
                if (isPickedUp) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "👤 Иван (Клиент)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(text = order.address, color = Color(0xFF374151), fontSize = 14.sp)
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Комментарий к заказу", fontWeight = FontWeight.Bold, color = Color(0xFF9139BA))
                                Spacer(Modifier.height(8.dp))
                                Text(text = "Громко постучите, буду спать...", color = Color(0xFF4B5563), fontSize = 14.sp)
                            }
                        }
                    }
                }

                // СОСТАВ ЗАКАЗА - Виден всегда
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Состав заказа", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Пицца Маргарита 30см", color = Color(0xFF374151))
                                Text(text = "×1", color = Color.Gray)
                            }
                            HorizontalDivider(Modifier.padding(vertical = 12.dp), color = Color(0xFFF3F4F6))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "К оплате:", fontWeight = FontWeight.SemiBold)
                                Text(text = "350 ₽", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }

            // НИЖНЯЯ ПАНЕЛЬ (Кнопка и доход)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Плашка дохода (Градиентная)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)
                        .background(purpleGradient, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(text = "Ваш доход", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                        Text(text = "${order.price} ₽", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(48.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "₽", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Свайпер (Тот самый компонент)
                SwipeButton(
                    text = if (isPickedUp) "Отдал заказ" else "Забрал заказ",
                    onSwipeComplete = {
                        if (!isPickedUp) {
                            isPickedUp = true
                        } else {
                            viewModel.completeOrder()
                            navController.navigate("main") {
                                popUpTo("main") { inclusive = true }
                            }
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Подтвердите действие свайпом",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun SwipeButton(text: String, onSwipeComplete: () -> Unit) {
    val density = LocalDensity.current
    var widthPx by remember { mutableStateOf(0f) }
    val handleSize = 50.dp
    val handleSizePx = with(density) { handleSize.toPx() }
    var offsetX by remember { mutableStateOf(0f) }

    // Сбрасываем положение ползунка при смене текста кнопки
    LaunchedEffect(text) { offsetX = 0f }

    val swipeLimit = (widthPx - handleSizePx - with(density) { 16.dp.toPx() }).coerceAtLeast(0f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color(0xFFF3F4F6), CircleShape)
            .padding(6.dp)
            .onGloballyPositioned { widthPx = it.size.width.toFloat() }
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            color = Color(0xFF4B5563),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .size(handleSize)
                .shadow(6.dp, CircleShape)
                .background(Color(0xFF9139BA), CircleShape)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX = (offsetX + delta).coerceIn(0f, swipeLimit)
                    },
                    onDragStopped = {
                        if (offsetX > swipeLimit * 0.85f) {
                            offsetX = swipeLimit
                            onSwipeComplete()
                        } else {
                            offsetX = 0f
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "→", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}