package com.example.insercodechirinosestobar.repository

import com.example.insercodechirinosestobar.model.Rol
import com.example.insercodechirinosestobar.model.Usuario
import kotlinx.coroutines.delay


class CredencialesException : Exception("Credenciales inválidas")
class ConexionException : Exception("Sin conexión")


class UsuarioRepository {

    private val usuarios = listOf(
        Usuario("admin@guardian.test", "123456", Rol.ADMIN),
        Usuario("supervisor@guardian.test", "123456", Rol.SUPERVISOR),
        Usuario("operador@guardian.test", "123456", Rol.OPERADOR)
    )

    suspend fun autenticar(email: String, password: String, simularSinConexion: Boolean): Usuario {
        delay(1500) // simula el tiempo de espera de la red
        if (simularSinConexion) throw ConexionException()
        return usuarios.firstOrNull {
            it.email.equals(email.trim(), ignoreCase = true) && it.password == password
        } ?: throw CredencialesException()
    }
}
