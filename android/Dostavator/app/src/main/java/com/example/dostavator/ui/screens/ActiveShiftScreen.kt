package com.example.dostavator.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dostavator.ui.components.BottomNavigationBar
import com.example.dostavator.viewmodel.OrderItem
import com.example.dostavator.viewmodel.ShiftViewModel

@Composable
fun ActiveShiftScreen(navController: NavController, viewModel: ShiftViewModel) {
    val grayText = Color(0xFF9CA3AF)
    val bgColor = Color(0xFFF9FAFB)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedTab = 0, onTabSelected = { index ->
                if (index == 1) navController.navigate("profile")
            })
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().background(bgColor).padding(padding)) {
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(16.dp)) {
                item {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Surface(color = Color(0xFF9139BA), shape = RoundedCornerShape(99.dp)) {
                                Text("НА СМЕНЕ", color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("ЗАРАБОТАНО СЕГОДНЯ: ${viewModel.todayEarnings} ₽", fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (viewModel.availableOrders.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillParentMaxHeight(0.6f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text("Пока пусто", color = Color.Gray, fontSize = 18.sp)
                        }
                    }
                } else {
                    items(viewModel.availableOrders) { order ->
                        OrderCard(order = order, onAcceptClick = { viewModel.acceptOrder(order) })
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.stopShift()
                    navController.navigate("main") { popUpTo("main") { inclusive = true } }
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("ЗАВЕРШЕНИЕ", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun OrderCard(order: OrderItem, onAcceptClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = order.id, color = Color.Gray, fontSize = 14.sp)
                Text(text = "${order.price} ₽", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Text(text = order.address, modifier = Modifier.padding(vertical = 8.dp), fontSize = 14.sp)
            Button(onClick = onAcceptClick, modifier = Modifier.align(Alignment.End), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9139BA)), shape = RoundedCornerShape(8.dp)) {
                Text("ВЗЯТЬ ЗАКАЗ", fontSize = 12.sp)
            }
        }
    }
}