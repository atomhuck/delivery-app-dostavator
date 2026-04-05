package com.example.dostavator.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.dostavator.R
import com.example.dostavator.ui.components.BottomNavigationBar
import com.example.dostavator.viewmodel.ShiftViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ShiftViewModel) {
    val bgColor = Color(0xFFF9FAFB)
    var showEditDialog by remember { mutableStateOf(false) }
    var tempDetails by remember { mutableStateOf(viewModel.paymentDetails) }
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { viewModel.updateProfileImage(it) }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Изменить реквизиты") },
            text = {
                OutlinedTextField(
                    value = tempDetails,
                    onValueChange = { tempDetails = it },
                    label = { Text("Номер счета/карты") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.updatePaymentDetails(tempDetails)
                    showEditDialog = false
                }) { Text("Сохранить") }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) { Text("Отмена") }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Профиль", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedTab = 1, onTabSelected = { index ->
                if (index == 0) navController.navigate("active_shift")
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()), // Добавил скролл на случай маленьких экранов
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Аватарка
            Box(modifier = Modifier.size(100.dp)) {
                if (viewModel.profileImageUri != null) {
                    AsyncImage(
                        model = viewModel.profileImageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFFE5E7EB), CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.avatar_placeholder),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Text(
                text = "изм.",
                modifier = Modifier.clickable { photoPicker.launch("image/*") }.padding(8.dp),
                color = Color(0xFF9139BA),
                fontSize = 12.sp
            )

            Text(viewModel.userName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "Привет, курьер! Пора выполнять\nзаказы",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                lineHeight = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ПУНКТЫ МЕНЮ
            ProfileItem("Архив") {
                navController.navigate("archive")
            }

            ProfileItem("Реквизиты: ${viewModel.paymentDetails}", "(изм)") {
                tempDetails = viewModel.paymentDetails
                showEditDialog = true
            }

            ProfileItem("Пригласить друга") { /* Логика */ }

            ProfileItem("Выйти") {
                viewModel.logout()
                navController.navigate("auth") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }
}

// ВОТ ЭТА ФУНКЦИЯ БЫЛА ПРОПУЩЕНА:
@Composable
fun ProfileItem(text: String, suffix: String = "", onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            if (suffix.isNotEmpty()) {
                Text(
                    text = suffix,
                    color = Color(0xFF9139BA),
                    fontSize = 16.sp
                )
            }
        }
    }
}