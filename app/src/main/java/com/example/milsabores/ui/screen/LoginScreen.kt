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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.milsabores.ui.viewmodel.LoginEvent
import com.example.milsabores.ui.viewmodel.LoginViewModel

/**
 * Esta es la pantalla (Vista) "tonta".
 * Solo muestra el 'uiState' y envía 'eventos' al ViewModel.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegistro: () -> Unit
) {
    // 1. "Observamos" el estado del ViewModel.
    // uiState se actualizará automáticamente gracias a collectAsState.
    val uiState by viewModel.uiState.collectAsState()

    // 2. Efecto para navegar (solo se dispara si loginExitoso cambia a true)
    LaunchedEffect(uiState.loginExitoso) {
        if (uiState.loginExitoso) {
            onLoginSuccess()
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_mil_sabores),
                contentDescription = "Logo Mil Sabores",
                modifier = Modifier.size(80.dp)
            )


            Text("¡Bienvenida!", style = MaterialTheme.typography.headlineMedium)
            Text("Ingresa a tu cuenta", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Campo Email (controlado por el ViewModel)
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onLoginEvent(LoginEvent.OnEmailChange(it)) }, // Envía un evento al cambiar
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = uiState.error != null, // El campo se marca en rojo si hay un error
                leadingIcon = {
                    Icon(Icons.Outlined.Email, "Email")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 4. Campo Contraseña (controlado por el ViewModel)
            var passwordVisible by rememberSaveable { mutableStateOf(false) }

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onLoginEvent(LoginEvent.OnPasswordChange(it)) }, // Envía un evento
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = uiState.error != null,
                // --- ¡NUEVO! ---
                leadingIcon = {
                    Icon(Icons.Outlined.Lock, "Contraseña")
                },
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, "Mostrar/Ocultar contraseña")
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 5. Botón de Login (o indicador de carga)
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.onLoginEvent(LoginEvent.OnLoginClick) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("INGRESAR")
                }
            }

            // 6. Mensaje de Error (solo se muestra si existe)
            uiState.error?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 7. Botón para ir a Registro
            TextButton(onClick = onNavigateToRegistro) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}