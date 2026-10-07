package com.example.insercodechirinosestobar.navigation

// Eventos de navegación que emite el ViewModel y ejecuta el NavController
sealed class NavigationEvent {
    data class NavigateTo(
        val route: String,
        val popUpTo: String? = null,   // ruta hasta donde se limpia la pila
        val inclusive: Boolean = false // si también se elimina esa ruta
    ) : NavigationEvent()

    object NavigateBack : NavigationEvent()
}
