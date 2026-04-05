package com.example.dostavator.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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

    val order = viewModel.currentActiveOrder ?: return

    val purpleGradient = Brush.horizontalGradient(
        listOf(Color(0xFFA357C6), Color(0xFF9139BA))
    )

    // Запрещаем возвращаться назад кнопкой телефона
    BackHandler(enabled = true) {
        // Пусто: назад нельзя, пока заказ не будет выполнен
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Заказ ${order.id}",
                        fontWeight = FontWeight.Bold
                    )
                },
                // Кнопку назад мы убрали по твоему запросу
                navigationIcon = { }
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
                contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 220.dp)
            ) {

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Точка сбора",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Text(
                                text = order.address,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                item { Spacer(Modifier.height(16.dp)) }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Состав заказа",
                                fontWeight = FontWeight.Bold
                            )

                            order.items.forEach { item ->
                                Text(
                                    text = item,
                                    modifier = Modifier.padding(top = 8.dp),
                                    color = Color(0xFF374151)
                                )
                            }
                        }
                    }
                }
            }

            // Нижняя панель с кнопкой
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)
                        .background(purpleGradient, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Вы получите",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${order.price} ₽",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(48.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "₽",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                SwipeButton(text = "Забрал заказ") {
                    viewModel.completeOrder()
                    // Используем путь active_shift, как в MainActivity
                    navController.navigate("active_shift") {
                        popUpTo("active_shift") { inclusive = true }
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeButton(text: String, onSwipeComplete: () -> Unit) {

    val density = LocalDensity.current
    var widthPx by remember { mutableStateOf(0f) }
    val handleSize = 48.dp
    val handleSizePx = with(density) { handleSize.toPx() }

    var offsetX by remember { mutableStateOf(0f) }
    val swipeLimit = (widthPx - handleSizePx).coerceAtLeast(0f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFFF3F4F6), CircleShape)
            .padding(4.dp)
            .onGloballyPositioned { coordinates ->
                widthPx = coordinates.size.width.toFloat()
            }
    ) {

        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            color = Color.Gray,
            fontSize = 15.sp
        )

        // Индикатор заполнения
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(with(density) { (offsetX + handleSizePx).toDp() })
                .background(Color(0xFF4ADE80), CircleShape)
        )

        // Ползунок
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .size(handleSize)
                .shadow(4.dp, CircleShape)
                .background(Color(0xFF9139BA), CircleShape)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        if (widthPx > 0) {
                            offsetX = (offsetX + delta).coerceIn(0f, swipeLimit)
                        }
                    },
                    onDragStopped = {
                        if (widthPx > 0 && offsetX > swipeLimit * 0.9f) {
                            offsetX = swipeLimit
                            onSwipeComplete()
                        } else {
                            offsetX = 0f
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "→",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}