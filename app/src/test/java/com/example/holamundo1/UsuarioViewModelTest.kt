package com.example.holamundo1.viewmodel

import com.example.holamundo1.data.PreferencesManager
import com.example.holamundo1.data.UsuarioDao
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsuarioViewModelTest {

    @Test
    fun validarFormulario_debeRetornarFalse_siCamposEstanVacios() = runTest {
        // 1. Crear dependencias simuladas (Mocks)
        val mockDao = mockk<UsuarioDao>(relaxed = true)
        val mockPrefManager = mockk<PreferencesManager>(relaxed = true)

        // 2. Instanciar el ViewModel con los Mocks
        val viewModel = UsuarioViewModel(
            usuarioDao = mockDao,
            preferencesManager = mockPrefManager
        )

        // 3. Ejecutar y verificar
        val esValido = viewModel.validarFormulario()
        assertFalse(esValido)
    }
}