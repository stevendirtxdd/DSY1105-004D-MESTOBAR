package com.example.insercodechirinosestobar.model

// Errores de validación por campo (null = sin error)
data class LoginErrores(
    val email: String? = null,
    val password: String? = null
)
