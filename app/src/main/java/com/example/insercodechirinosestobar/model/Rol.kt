package com.example.insercodechirinosestobar.model

// Roles del sistema Modo Guardián; cada rol ve un Home distinto
enum class Rol(val titulo: String, val funciones: List<String>) {
    ADMIN(
        "Administrador",
        listOf("Gestionar usuarios de prueba", "Administrar roles y permisos")
    ),
    SUPERVISOR(
        "Supervisor",
        listOf("Revisar indicadores", "Hacer seguimiento de la operación")
    ),
    OPERADOR(
        "Operador",
        listOf("Ver tareas asignadas", "Actualizar estados de la operación")
    )
}
