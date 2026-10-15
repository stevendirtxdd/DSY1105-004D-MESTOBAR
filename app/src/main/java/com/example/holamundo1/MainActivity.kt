package com.example.holamundo1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.holamundo1.navigation.Screen
import com.example.holamundo1.ui.screens.PerfilScreen
import com.example.holamundo1.ui.theme.Holamundo1Theme
import com.example.holamundo1.viewmodel.PerfilViewModel

class MainActivity : ComponentActivity() {

    private val perfilViewModel: PerfilViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Holamundo1Theme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Profile.route // 👈 Usa la ruta "profile_page" de tu Screen.kt
                ) {
                    composable(Screen.Profile.route) {
                        PerfilScreen(viewModel = perfilViewModel)
                    }
                    // Aquí puedes mantener tus otras rutas (Home, Settings, etc.)
                }
            }
        }
    }
}