package com.example.holamundo1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.holamundo1.model.EstadoDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EstadoViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = EstadoDataStore(application)

    private val _activo = MutableStateFlow(false)
    val activo: StateFlow<Boolean> = _activo.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _mensajeGuardado = MutableStateFlow(false)
    val mensajeGuardado: StateFlow<Boolean> = _mensajeGuardado.asStateFlow()

    init {
        cargarEstadoInicial()
    }

    private fun cargarEstadoInicial() {
        viewModelScope.launch {
            _cargando.value = true
            delay(1500) // Simulación de carga inicial
            dataStore.obtenerEstado().collect { guardado ->
                _activo.value = guardado ?: false
                _cargando.value = false
            }
        }
    }

    fun cambiarEstado() {
        viewModelScope.launch {
            val nuevoValor = !_activo.value
            _activo.value = nuevoValor
            dataStore.guardarEstado(nuevoValor)

            // Feedback animado
            _mensajeGuardado.value = true
            delay(2000)
            _mensajeGuardado.value = false
        }
    }
}