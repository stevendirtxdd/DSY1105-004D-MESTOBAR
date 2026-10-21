package com.example.holamundo1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.holamundo1.ui.screens.ListadoOfflineScreen
import com.example.holamundo1.ui.screens.RegistroIntegralScreen
import com.example.holamundo1.ui.screens.ResumenScreen
import com.example.holamundo1.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(viewModel: UsuarioViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "registro") {
        composable("registro") {
            RegistroIntegralScreen(
                viewModel = viewModel,
                onNavegarResumen = { navController.navigate("resumen") },
                onNavegarListado = { navController.navigate("listado") }
            )
        }
        composable("resumen") {
            ResumenScreen(
                viewModel = viewModel,
                onVolver = { navController.popBackStack() }
            )
        }
        composable("listado") {
            ListadoOfflineScreen(
                viewModel = viewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}