package com.example.insercodechirinosestobar.navigation


sealed class NavigationEvent {
    data class NavigateTo(
        val route: String,
        val popUpTo: String? = null,
        val inclusive: Boolean = false
    ) : NavigationEvent()

    object NavigateBack : NavigationEvent()
}
