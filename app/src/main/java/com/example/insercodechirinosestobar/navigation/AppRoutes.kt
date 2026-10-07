package com.example.insercodechirinosestobar.navigation

// Rutas de la app (sealed class para evitar errores de texto)
sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object Home : AppRoutes("home")
}
