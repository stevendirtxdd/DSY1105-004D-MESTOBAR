package com.example.holamundo1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.holamundo1.data.AppDatabase
import com.example.holamundo1.data.PreferencesManager
import com.example.holamundo1.navigation.AppNavigation
import com.example.holamundo1.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(context = this)
        val prefManager = PreferencesManager(context = this)

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return UsuarioViewModel(usuarioDao = db.usuarioDao(), preferencesManager = prefManager) as T
            }
        }

        val viewModel = ViewModelProvider(owner = this, factory)[UsuarioViewModel::class.java]

        setContent {
            MaterialTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}