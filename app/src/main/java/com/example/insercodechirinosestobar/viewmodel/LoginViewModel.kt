package com.example.insercodechirinosestobar.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.insercodechirinosestobar.model.LoginErrores
import com.example.insercodechirinosestobar.model.LoginUiState
import com.example.insercodechirinosestobar.repository.ConexionException
import com.example.insercodechirinosestobar.repository.CredencialesException
import com.example.insercodechirinosestobar.repository.SesionRepository
import com.example.insercodechirinosestobar.repository.UsuarioRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel compartido: lo usan LoginScreen y HomeScreen (el Home lee el rol desde aquí)
class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val usuarioRepository = UsuarioRepository()
    private val sesionRepository = SesionRepository(app.applicationContext)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _errores = MutableStateFlow(LoginErrores())
    val errores: StateFlow<LoginErrores> = _errores.asStateFlow()

    init {
        // Si el usuario pidió recordar su email, se carga al abrir la app
        viewModelScope.launch {
            try {
                val email = sesionRepository.emailGuardado.first()
                if (email.isNotBlank()) {
                    _uiState.update {
                        if (it.email.isBlank()) it.copy(email = email, recordarEmail = true) else it
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // La persistencia es opcional: si falla, la app sigue funcionando
            }
        }
    }

    fun onEmailChange(valor: String) {
        _uiState.update { it.copy(email = valor, mensajeError = null) }
        _errores.update { it.copy(email = null) }
    }

    fun onPasswordChange(valor: String) {
        _uiState.update { it.copy(password = valor, mensajeError = null) }
        _errores.update { it.copy(password = null) }
    }

    fun onRecordarEmailChange(valor: Boolean) {
        _uiState.update { it.copy(recordarEmail = valor) }
    }

    fun onSimularSinConexionChange(valor: Boolean) {
        _uiState.update { it.copy(simularSinConexion = valor, mensajeError = null) }
    }

    // Valida los campos, actualiza los errores y retorna true si todo está correcto
    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        val errores = LoginErrores(
            email = if (!Patterns.EMAIL_ADDRESS.matcher(estado.email.trim()).matches())
                "Ingresa un correo válido" else null,
            password = if (estado.password.length < 6)
                "La clave debe tener al menos 6 caracteres" else null
        )
        _errores.value = errores
        return errores.email == null && errores.password == null
    }

    fun iniciarSesion() {
        if (_uiState.value.isLoading) return // evita doble click mientras carga
        if (!validarFormulario()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, mensajeError = null) }
            val estado = _uiState.value
            try {
                val usuario = usuarioRepository.autenticar(
                    estado.email, estado.password, estado.simularSinConexion
                )
                // Persistencia limitada: solo email; un fallo aquí no debe romper el login
                runCatching {
                    if (estado.recordarEmail) sesionRepository.guardarEmail(estado.email.trim())
                    else sesionRepository.borrarEmail()
                }
                // Se limpia la clave y se publica el rol; el Home lo observa
                _uiState.update { it.copy(isLoading = false, password = "", rol = usuario.rol) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: CredencialesException) {
                mostrarError("Correo o clave incorrectos")
            } catch (e: ConexionException) {
                mostrarError("Error de conexión. Revisa tu red e inténtalo de nuevo")
            } catch (e: Exception) {
                // Cualquier otro error: la app no se cae ni se reinicia
                mostrarError("Ocurrió un error inesperado. Inténtalo nuevamente")
            }
        }
    }

    private fun mostrarError(mensaje: String) {
        _uiState.update { it.copy(isLoading = false, mensajeError = mensaje) }
    }

    // Cierra la sesión: el Home detecta rol = null y vuelve al login
    fun cerrarSesion() {
        _uiState.update { it.copy(rol = null, password = "", mensajeError = null) }
        _errores.value = LoginErrores()
    }
}
