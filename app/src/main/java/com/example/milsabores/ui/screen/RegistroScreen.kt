package com.example.milsabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.milsabores.R
import com.example.milsabores.ui.viewmodel.RegistroEvent
import com.example.milsabores.ui.viewmodel.RegistroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel,
    onRegistroSuccess: () -> Unit,
    onVolverALogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registroExitoso) {
        if (uiState.registroExitoso) {
            onRegistroSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                // 1. Título con la fuente Pacifico
                title = {
                    Text(
                        "Crear cuenta",
                        fontFamily = MaterialTheme.typography.headlineMedium.fontFamily
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolverALogin) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                // 2. Colores de la barra (rosa y blanco/crema)
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo_mil_sabores),
                    contentDescription = "Logo Mil Sabores",
                    modifier = Modifier.size(80.dp)
                )

                Text(
                    text = "Completa tus datos",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Campo Nombre
                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = { viewModel.onRegistroEvent(RegistroEvent.OnNombreChange(it)) },
                    label = { Text("Nombre Completo *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = uiState.error != null,
                    leadingIcon = {
                        Icon(Icons.Outlined.Person, "Nombre")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Email
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onRegistroEvent(RegistroEvent.OnEmailChange(it)) },
                    label = { Text("Email *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = uiState.error != null,
                    leadingIcon = {
                        Icon(Icons.Outlined.Email, "Email")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Contraseña
                var passwordVisible by rememberSaveable { mutableStateOf(false) }
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onRegistroEvent(RegistroEvent.OnPasswordChange(it)) },
                    label = { Text("Contraseña *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = uiState.error != null,
                    leadingIcon = {
                        Icon(Icons.Outlined.Lock, "Contraseña")
                    },
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, "Mostrar/Ocultar")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Confirmar Contraseña
                var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
                OutlinedTextField(
                    value = uiState.confirmarPassword,
                    onValueChange = { viewModel.onRegistroEvent(RegistroEvent.OnConfirmarPasswordChange(it)) },
                    label = { Text("Confirmar Contraseña *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = uiState.error != null,
                    leadingIcon = {
                        Icon(Icons.Outlined.Lock, "Contraseña")
                    },
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(imageVector = image, "Mostrar/Ocultar")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Dirección (Opcional)
                OutlinedTextField(
                    value = uiState.direccion,
                    onValueChange = { viewModel.onRegistroEvent(RegistroEvent.OnDireccionChange(it)) },
                    label = { Text("Dirección (Opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = false,
                    leadingIcon = {
                        Icon(Icons.Outlined.Home, "Dirección")
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de Registro
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { viewModel.onRegistroEvent(RegistroEvent.OnRegistroClick) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("REGISTRARME")
                    }
                }

                // Mensaje de Error
                uiState.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Botón para volver al Login
                TextButton(onClick = onVolverALogin) {
                    Text("¿Ya tienes cuenta? Inicia sesión")
                }
            }
        }
    }
}