package com.example.insercodechirinosestobar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.insercodechirinosestobar.navigation.AppRoutes
import com.example.insercodechirinosestobar.navigation.NavigationEvent
import com.example.insercodechirinosestobar.navigation.NavigationViewModel
import com.example.insercodechirinosestobar.ui.screens.HomeScreen
import com.example.insercodechirinosestobar.ui.screens.LoginScreen
import com.example.insercodechirinosestobar.ui.theme.InserCodeTheme
import com.example.insercodechirinosestobar.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InserCodeTheme {
                val navController = rememberNavController()
                val navigationViewModel: NavigationViewModel = viewModel()

                val loginViewModel: LoginViewModel = viewModel()


                LaunchedEffect(Unit) {
                    navigationViewModel.navigationEvents.collect { evento ->
                        when (evento) {
                            is NavigationEvent.NavigateTo -> navController.navigate(evento.route) {
                                launchSingleTop = true
                                evento.popUpTo?.let { popUpTo(it) { inclusive = evento.inclusive } }
                            }
                            NavigationEvent.NavigateBack -> navController.popBackStack()
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = AppRoutes.Login.route
                ) {
                    composable(AppRoutes.Login.route) {
                        LoginScreen(
                            viewModel = loginViewModel,
                            onLoginExitoso = {

                                navigationViewModel.navigateTo(
                                    AppRoutes.Home.route,
                                    popUpTo = AppRoutes.Login.route,
                                    inclusive = true
                                )
                            }
                        )
                    }
                    composable(AppRoutes.Home.route) {
                        HomeScreen(
                            viewModel = loginViewModel,
                            onSinSesion = {

                                navigationViewModel.navigateTo(
                                    AppRoutes.Login.route,
                                    popUpTo = AppRoutes.Home.route,
                                    inclusive = true
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
