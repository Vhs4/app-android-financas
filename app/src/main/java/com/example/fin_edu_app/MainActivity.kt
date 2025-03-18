package com.example.fin_edu_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fin_edu_app.ui.theme.FineduappTheme
import com.example.fin_edu_app.ui.theme.pages.CreateAccount
import com.example.fin_edu_app.ui.theme.pages.HomeScreen
import com.example.fin_edu_app.ui.theme.pages.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FineduappTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController) }
                    composable("home") { HomeScreen() }
                    composable("register") { CreateAccount(navController) }

                }
            }
        }
    }
}
