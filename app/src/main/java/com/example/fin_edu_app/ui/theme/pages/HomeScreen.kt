package com.example.fin_edu_app.ui.theme.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Criar conta",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFC107),
            modifier = Modifier.clickable { navController.navigate("quiz") }
        )
    }
}
