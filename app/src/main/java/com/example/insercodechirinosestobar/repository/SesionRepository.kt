package com.example.insercodechirinosestobar.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "sesion")

// Persistencia local LIMITADA: solo se guarda el email (nunca la clave)
class SesionRepository(private val context: Context) {

    private val claveEmail = stringPreferencesKey("email_recordado")

    val emailGuardado: Flow<String> = context.dataStore.data.map { it[claveEmail] ?: "" }

    suspend fun guardarEmail(email: String) {
        context.dataStore.edit { it[claveEmail] = email }
    }

    suspend fun borrarEmail() {
        context.dataStore.edit { it.remove(claveEmail) }
    }
}
