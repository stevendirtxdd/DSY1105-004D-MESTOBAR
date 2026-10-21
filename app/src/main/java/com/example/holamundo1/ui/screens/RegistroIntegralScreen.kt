package com.example.holamundo1.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.holamundo1.ui.components.ImagenInteligente
import com.example.holamundo1.ui.components.PremiumCard
import com.example.holamundo1.viewmodel.UsuarioViewModel
import java.io.File

@Composable
fun RegistroIntegralScreen(
    viewModel: UsuarioViewModel,
    onNavegarResumen: () -> Unit,
    onNavegarListado: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val errores by viewModel.errores.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val mensajeExito by viewModel.mensajeExito.collectAsState()

    var tempCameraUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val galeriaLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        viewModel.actualizarFoto(uri)
    }

    val camaraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
        if (exito && tempCameraUri != null) {
            viewModel.actualizarFoto(tempCameraUri)
        }
    }

    val colorBotonAnimado by animateColorAsState(
        targetValue = if (uiState.modoEspecial) Color(0xFF2E7D32) else Color.Gray,
        label = "colorBoton"
    )

    val textoDerivado by remember {
        derivedStateOf { if (uiState.modoEspecial) "Modo Premium Activo" else "Activar Modo Premium" }
    }

    if (cargando) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Registro Integral", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            ImagenInteligente(imagenUri = uiState.fotoUri)
            errores.fotoUri?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    val uri = crearUriTemporal(context)
                    tempCameraUri = uri
                    camaraLauncher.launch(uri)
                }) { Text("Cámara") }

                Button(onClick = { galeriaLauncher.launch("image/*") }) { Text("Galería") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre") },
                isError = errores.nombre != null,
                supportingText = { errores.nombre?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo Electrónico") },
                isError = errores.correo != null,
                supportingText = { errores.correo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.clave,
                onValueChange = viewModel::onClaveChange,
                label = { Text("Clave") },
                visualTransformation = PasswordVisualTransformation(),
                isError = errores.clave != null,
                supportingText = { errores.clave?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.direccion,
                onValueChange = viewModel::onDireccionChange,
                label = { Text("Dirección") },
                isError = errores.direccion != null,
                supportingText = { errores.direccion?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = uiState.aceptaTerminos, onCheckedChange = viewModel::onTerminosChange)
                Text("Acepto términos y condiciones")
            }
            errores.aceptaTerminos?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = viewModel::toggleModoEspecial,
                colors = ButtonDefaults.buttonColors(containerColor = colorBotonAnimado),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(textoDerivado, color = Color.White)
            }

            AnimatedVisibility(visible = mensajeExito) {
                Text(
                    "¡Ajustes guardados en DataStore!",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }

            PremiumCard(visible = uiState.modoEspecial)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.guardarEnRoom(onExito = onNavegarResumen) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar y ver Resumen")
            }

            OutlinedButton(
                onClick = onNavegarListado,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Ver Lista Registrada Offline (ROOM)")
            }
        }
    }
}

private fun crearUriTemporal(context: Context): Uri {
    val tempFile = File.createTempFile("JPEG_${System.currentTimeMillis()}_", ".jpg", context.externalCacheDir)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempFile)
}