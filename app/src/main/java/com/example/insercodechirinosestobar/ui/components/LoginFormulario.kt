package com.example.insercodechirinosestobar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.insercodechirinosestobar.model.LoginErrores
import com.example.insercodechirinosestobar.model.LoginUiState

// Agrupa las acciones del formulario (con valores por defecto para los previews)
data class LoginAcciones(
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onRecordarChange: (Boolean) -> Unit = {},
    val onSimularChange: (Boolean) -> Unit = {},
    val onIngresar: () -> Unit = {}
)

// Formulario reutilizado por los dos layouts (Compact y Medium/Expanded)
@Composable
fun LoginFormulario(
    estado: LoginUiState,
    errores: LoginErrores,
    acciones: LoginAcciones,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Campo de correo con error de validación
        OutlinedTextField(
            value = estado.email,
            onValueChange = acciones.onEmailChange,
            label = { Text("Correo") },
            isError = errores.email != null,
            supportingText = { errores.email?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            enabled = !estado.isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        // Campo de clave oculta con PasswordVisualTransformation
        OutlinedTextField(
            value = estado.password,
            onValueChange = acciones.onPasswordChange,
            label = { Text("Clave") },
            isError = errores.password != null,
            supportingText = { errores.password?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            enabled = !estado.isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        // Persistencia local limitada: solo se recuerda el correo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = estado.recordarEmail,
                onCheckedChange = acciones.onRecordarChange,
                enabled = !estado.isLoading
            )
            Text("Recordar mi correo")
        }

        // Interruptor para probar el error de conectividad
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = estado.simularSinConexion,
                onCheckedChange = acciones.onSimularChange,
                enabled = !estado.isLoading
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Simular sin conexión (prueba)", style = MaterialTheme.typography.bodyMedium)
        }

        // Mensaje de error general (credenciales, conexión, error inesperado)
        estado.mensajeError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Botón habilitado solo si isLoginEnabled; muestra carga mientras autentica
        Button(
            onClick = acciones.onIngresar,
            enabled = estado.isLoginEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (estado.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ingresando...")
            } else {
                Text("Iniciar sesión")
            }
        }

        Text(
            text = "Usuarios de prueba: admin@guardian.test, supervisor@guardian.test, " +
                "operador@guardian.test (clave 123456)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
