package com.example.holamundo1.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holamundo1.data.PreferencesManager
import com.example.holamundo1.data.UsuarioDao
import com.example.holamundo1.data.UsuarioEntity
import com.example.holamundo1.model.UsuarioErrores
import com.example.holamundo1.model.UsuarioUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val usuarioDao: UsuarioDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    private val _errores = MutableStateFlow(UsuarioErrores())
    val errores: StateFlow<UsuarioErrores> = _errores.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _mensajeExito = MutableStateFlow(false)
    val mensajeExito: StateFlow<Boolean> = _mensajeExito.asStateFlow()

    val usuariosLista = usuarioDao.getUsuariosFlow()

    init {
        viewModelScope.launch {
            // Carga inicial simulada
            delay(1500)
            _cargando.value = false
        }

        viewModelScope.launch {
            preferencesManager.modoEspecialFlow.collectLatest { activo ->
                _uiState.value = _uiState.value.copy(modoEspecial = activo)
            }
        }
    }

    fun onNombreChange(v: String) { _uiState.value = _uiState.value.copy(nombre = v) }
    fun onCorreoChange(v: String) { _uiState.value = _uiState.value.copy(correo = v) }
    fun onClaveChange(v: String) { _uiState.value = _uiState.value.copy(clave = v) }
    fun onDireccionChange(v: String) { _uiState.value = _uiState.value.copy(direccion = v) }
    fun onTerminosChange(v: Boolean) { _uiState.value = _uiState.value.copy(aceptaTerminos = v) }
    fun actualizarFoto(uri: Uri?) { _uiState.value = _uiState.value.copy(fotoUri = uri) }

    fun toggleModoEspecial() {
        viewModelScope.launch {
            val nuevoValor = !_uiState.value.modoEspecial
            preferencesManager.guardarModoEspecial(nuevoValor)
            _mensajeExito.value = true
            delay(2000)
            _mensajeExito.value = false
        }
    }

    fun validarFormulario(): Boolean {
        val s = _uiState.value
        val eNombre = if (s.nombre.length < 3) "El nombre debe tener al menos 3 caracteres" else null
        val eCorreo = if (!s.correo.contains("@")) "Correo inválido (debe incluir @)" else null
        val eClave = if (s.clave.length < 6) "La clave debe contener al menos 6 caracteres" else null
        val eDireccion = if (s.direccion.isBlank()) "La dirección es obligatoria" else null
        val eTerminos = if (!s.aceptaTerminos) "Debe aceptar los términos y condiciones" else null
        val eFoto = if (s.fotoUri == null) "Debe seleccionar o capturar una foto" else null

        _errores.value = UsuarioErrores(
            nombre = eNombre,
            correo = eCorreo,
            clave = eClave,
            direccion = eDireccion,
            aceptaTerminos = eTerminos,
            fotoUri = eFoto
        )

        return listOfNotNull(eNombre, eCorreo, eClave, eDireccion, eTerminos, eFoto).isEmpty()
    }

    fun guardarEnRoom(onExito: () -> Unit) {
        if (!validarFormulario()) return

        viewModelScope.launch {
            val s = _uiState.value
            val entity = UsuarioEntity(
                nombre = s.nombre,
                correo = s.correo,
                direccion = s.direccion,
                modoEspecial = s.modoEspecial,
                fotoPath = s.fotoUri?.toString()
            )
            usuarioDao.insertar(entity)
            onExito()
        }
    }
}