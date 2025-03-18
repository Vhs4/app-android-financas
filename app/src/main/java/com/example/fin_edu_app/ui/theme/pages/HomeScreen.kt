package com.example.fin_edu_app.ui.theme.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = "Bem-vindo à Home!", style = MaterialTheme.typography.headlineMedium)
    }
}
