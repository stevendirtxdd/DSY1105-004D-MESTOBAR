package com.example.holamundo1.navigation

// Sealed class para definir rutas de forma segura en la navegación
sealed class Screen(val route: String) {
    data object Home : Screen(route = "home_page")
    data object Profile : Screen(route = "profile_page")
    data object Settings : Screen(route = "settings_page")
}