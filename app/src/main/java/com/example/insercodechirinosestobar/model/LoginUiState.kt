package com.example.insercodechirinosestobar.model

// Estado completo de la pantalla de login (se guarda en un MutableStateFlow)
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val recordarEmail: Boolean = false,
    val simularSinConexion: Boolean = false, // permite probar el error de conectividad
    val mensajeError: String? = null,        // error general (credenciales, conexión, etc.)
    val rol: Rol? = null                     // rol del usuario autenticado (lo lee el Home)
) {
    // El botón solo se habilita si hay datos y no se está cargando
    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading
}
