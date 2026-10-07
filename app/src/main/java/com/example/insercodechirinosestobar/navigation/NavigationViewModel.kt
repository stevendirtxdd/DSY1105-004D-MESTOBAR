package com.example.insercodechirinosestobar.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class NavigationViewModel : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    fun navigateTo(route: String, popUpTo: String? = null, inclusive: Boolean = false) {
        _navigationEvents.tryEmit(NavigationEvent.NavigateTo(route, popUpTo, inclusive))
    }

    fun navigateBack() {
        _navigationEvents.tryEmit(NavigationEvent.NavigateBack)
    }
}
