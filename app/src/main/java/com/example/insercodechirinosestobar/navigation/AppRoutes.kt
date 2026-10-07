package com.example.insercodechirinosestobar.navigation


sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object Home : AppRoutes("home")
}
