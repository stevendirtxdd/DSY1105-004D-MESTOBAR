package com.example.insercodechirinosestobar.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.insercodechirinosestobar.R
import com.example.insercodechirinosestobar.model.LoginErrores
import com.example.insercodechirinosestobar.model.LoginUiState
import com.example.insercodechirinosestobar.ui.components.LoginAcciones
import com.example.insercodechirinosestobar.ui.components.LoginFormulario
import com.example.insercodechirinosestobar.ui.theme.InserCodeTheme
import com.example.insercodechirinosestobar.ui.utils.obtenerWindowSizeClass
import com.example.insercodechirinosestobar.viewmodel.LoginViewModel


@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginExitoso: () -> Unit
) {
    val estado by viewModel.uiState.collectAsState()
    val errores by viewModel.errores.collectAsState()
    val windowSizeClass = obtenerWindowSizeClass()


    LaunchedEffect(estado.rol) {
        if (estado.rol != null) onLoginExitoso()
    }

    LoginContenido(
        estado = estado,
        errores = errores,
        ancho = windowSizeClass.widthSizeClass,
        acciones = LoginAcciones(
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onRecordarChange = viewModel::onRecordarEmailChange,
            onSimularChange = viewModel::onSimularSinConexionChange,
            onIngresar = viewModel::iniciarSesion
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContenido(
    estado: LoginUiState,
    errores: LoginErrores,
    ancho: WindowWidthSizeClass,
    acciones: LoginAcciones
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Modo Guardián") }) }
    ) { innerPadding ->
        when (ancho) {
            WindowWidthSizeClass.Compact ->
                LoginCompacta(estado, errores, acciones, Modifier.padding(innerPadding))
            else -> // Medium y Expanded
                LoginAmplia(estado, errores, acciones, Modifier.padding(innerPadding))
        }
    }
}

// Layout Compact (celular): todo en una Column vertical
@Composable
private fun LoginCompacta(
    estado: LoginUiState,
    errores: LoginErrores,
    acciones: LoginAcciones,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Modo Guardián",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "Bienvenido",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Inicia sesión para continuar",
            style = MaterialTheme.typography.bodyMedium
        )
        LoginFormulario(estado, errores, acciones)
    }
}


@Composable
private fun LoginAmplia(
    estado: LoginUiState,
    errores: LoginErrores,
    acciones: LoginAcciones,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalArrangement = Arrangement.spacedBy(48.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Modo Guardián",
                modifier = Modifier.height(160.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Bienvenido",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Card(modifier = Modifier.weight(1f).widthIn(max = 480.dp)) {
            LoginFormulario(
                estado = estado,
                errores = errores,
                acciones = acciones,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            )
        }
    }
}


@Preview(name = "Compact", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun PreviewLoginCompact() {
    InserCodeTheme {
        LoginContenido(LoginUiState(), LoginErrores(), WindowWidthSizeClass.Compact, LoginAcciones())
    }
}

@Preview(name = "Medium", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun PreviewLoginMedium() {
    InserCodeTheme {
        LoginContenido(LoginUiState(), LoginErrores(), WindowWidthSizeClass.Medium, LoginAcciones())
    }
}

@Preview(name = "Expanded", widthDp = 1100, heightDp = 800, showBackground = true)
@Composable
fun PreviewLoginExpanded() {
    InserCodeTheme {
        LoginContenido(LoginUiState(), LoginErrores(), WindowWidthSizeClass.Expanded, LoginAcciones())
    }
}
