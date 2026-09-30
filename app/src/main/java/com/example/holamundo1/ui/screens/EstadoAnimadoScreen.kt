package com.example.holamundo1.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.holamundo1.viewmodel.EstadoViewModel

@Composable
fun EstadoAnimadoScreen(viewModel: EstadoViewModel) {
    val activo by viewModel.activo.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val mensajeGuardado by viewModel.mensajeGuardado.collectAsState()

    // Estado derivado mediante derivedStateOf
    val textoEstado by remember {
        derivedStateOf { if (activo) "Modo Especial ACTIVADO" else "Modo Especial DESACTIVADO" }
    }

    // Animación de color según el estado
    val colorBoton by animateColorAsState(
        targetValue = if (activo) Color(0xFF4CAF50) else Color(0xFFE53935),
        animationSpec = tween(durationMillis = 500),
        label = "ColorBotonAnim"
    )

    if (cargando) {
        // Simulación de Carga Inicial
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Cargando preferencias...")
            }
        }
    } else {
        // UI Principal con Feedback Animado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = textoEstado,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.cambiarEstado() },
                colors = ButtonDefaults.buttonColors(containerColor = colorBoton),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = if (activo) "Desactivar Modo" else "Activar Modo",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Visibilidad animada del mensaje
            AnimatedVisibility(
                visible = mensajeGuardado,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "¡Estado guardado en DataStore!",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}