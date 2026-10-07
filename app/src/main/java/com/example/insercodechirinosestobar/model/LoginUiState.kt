package com.example.insercodechirinosestobar.model

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val recordarEmail: Boolean = false,
    val simularSinConexion: Boolean = false,
    val mensajeError: String? = null,
    val rol: Rol? = null
) {

    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading
}
