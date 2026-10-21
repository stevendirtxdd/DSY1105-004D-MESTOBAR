package com.example.holamundo1.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.holamundo1.ui.components.ImagenInteligente
import com.example.holamundo1.viewmodel.UsuarioViewModel

@Composable
fun ListadoOfflineScreen(viewModel: UsuarioViewModel, onVolver: () -> Unit) {
    val usuarios by viewModel.usuariosLista.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            Text(
                "Usuarios Registrados Offline (ROOM)",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (usuarios.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No hay registros guardados en la BD")
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                    items(usuarios) { item ->
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ImagenInteligente(
                                    imagenUri = item.fotoPath?.let { Uri.parse(it) },
                                    modifier = Modifier.size(60.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text(item.correo, style = MaterialTheme.typography.bodySmall)
                                    Text("Dirección: ${item.direccion}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }

            Button(onClick = onVolver, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Volver")
            }
        }
    }
}