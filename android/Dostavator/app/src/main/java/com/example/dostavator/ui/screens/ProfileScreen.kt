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
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { viewModel.updateProfileImage(it) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Профиль", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = bgColor)
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedTab = 1, onTabSelected = { if (it == 0) navController.navigate("main") })
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(bgColor).padding(padding).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.size(100.dp).border(2.dp, Color(0xFFE5E7EB), CircleShape)) {
                if (viewModel.profileImageUri != null) {
                    AsyncImage(model = viewModel.profileImageUri, contentDescription = null, modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                } else {
                    Image(painter = painterResource(R.drawable.avatar_placeholder), contentDescription = null, modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                }
            }

            Text("изм.", modifier = Modifier.clickable { photoPicker.launch("image/*") }.padding(8.dp), color = Color.Gray, fontSize = 12.sp)
            Text(viewModel.userName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Привет, курьер! Пора выполнять\nзаказы", textAlign = TextAlign.Center, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))

            Spacer(modifier = Modifier.height(24.dp))

            ProfileItem("Архив") {}
            ProfileItem("Реквизиты: ${viewModel.paymentDetails}", "(изм)") { /* диалог */ }
            ProfileItem("Пригласить друга") {}

            ProfileItem("Выйти") {
                viewModel.logout()
                // ИСПРАВЛЕНО: navigate("auth") вместо "auth_screen"
                navController.navigate("auth") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
}

@Composable
fun ProfileItem(text: String, suffix: String = "", onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            if (suffix.isNotEmpty()) Text(" $suffix", color = Color.Gray, fontSize = 16.sp)
        }
    }
}