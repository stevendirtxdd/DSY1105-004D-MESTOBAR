package com.example.insercodechirinosestobar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.insercodechirinosestobar.model.Rol
import com.example.insercodechirinosestobar.viewmodel.LoginViewModel

// Home: observa el MISMO LoginViewModel para conocer el rol (sin pasar argumentos por la ruta)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: LoginViewModel,
    onSinSesion: () -> Unit
) {
    val estado by viewModel.uiState.collectAsState()
    val rol = estado.rol

    // Si no hay rol (se cerró la sesión) se vuelve al Login
    LaunchedEffect(rol) {
        if (rol == null) onSinSesion()
    }
    if (rol == null) return

    // Cada rol tiene un color distinto tomado del MaterialTheme
    val colorRol = when (rol) {
        Rol.ADMIN -> MaterialTheme.colorScheme.primary
        Rol.SUPERVISOR -> MaterialTheme.colorScheme.secondary
        Rol.OPERADOR -> MaterialTheme.colorScheme.tertiary
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Inicio · " + rol.titulo) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Hola, " + rol.titulo,
                    color = colorRol,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Sesión: " + estado.email,
                    style = MaterialTheme.typography.bodyMedium
                )

                // Opciones disponibles según el rol
                Card(
                    colors = CardDefaults.cardColors(containerColor = colorRol),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Qué puedes hacer",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.surface
                        )
                        rol.funciones.forEach { funcion ->
                            Text(
                                text = "• " + funcion,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                }

                // Al cerrar sesión el rol pasa a null y el efecto de arriba navega al Login
                Button(
                    onClick = viewModel::cerrarSesion,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}
