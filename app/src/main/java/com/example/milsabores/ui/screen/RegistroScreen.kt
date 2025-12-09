package com.example.milsabores.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milsabores.R
import com.example.milsabores.ui.components.CameraPreview // <-- Asegúrate de tener este componente
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
    val context = LocalContext.current

    // Estado para controlar la cámara
    var mostrarCamara by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.registroExitoso) {
        if (uiState.registroExitoso) {
            onRegistroSuccess()
        }
    }

    if (mostrarCamara) {
        // --- VISTA DE CÁMARA ---
        CameraPreview(
            onImageCaptured = { uri ->
                viewModel.onRegistroEvent(RegistroEvent.OnFotoCapturada(uri.toString()))
                mostrarCamara = false
            },
            onError = {
                Toast.makeText(context, "Error al tomar foto", Toast.LENGTH_SHORT).show()
                mostrarCamara = false
            }
        )
        // Botón Cancelar
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Button(
                onClick = { mostrarCamara = false },
                modifier = Modifier.align(Alignment.TopEnd)
            ) { Text("Cancelar") }
        }

    } else {
        // --- VISTA DE FORMULARIO ---
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Crear cuenta") },
                    navigationIcon = {
                        IconButton(onClick = onVolverALogin) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                        }
                    },
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
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- SECCIÓN FOTO DE PERFIL ---
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clickable { mostrarCamara = true },
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.fotoUri != null) {
                            // Foto capturada
                            AsyncImage(
                                model = ImageRequest.Builder(context).data(Uri.parse(uiState.fotoUri)).build(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().clip(CircleShape).border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Placeholder (Logo o Icono)
                            Image(
                                painter = painterResource(id = R.drawable.logo_mil_sabores),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().clip(CircleShape).border(2.dp, Color.Gray, CircleShape)
                            )
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp)
                            )
                        }
                    }
                    Text("Toca para agregar foto", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- CAMPOS DEL FORMULARIO (Igual que antes) ---
                    OutlinedTextField(
                        value = uiState.nombre,
                        onValueChange = { viewModel.onRegistroEvent(RegistroEvent.OnNombreChange(it)) },
                        label = { Text("Nombre Completo *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = uiState.error != null,
                        leadingIcon = { Icon(Icons.Outlined.Person, null) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = { viewModel.onRegistroEvent(RegistroEvent.OnEmailChange(it)) },
                        label = { Text("Email *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = uiState.error != null,
                        leadingIcon = { Icon(Icons.Outlined.Email, null) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password
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
                        leadingIcon = { Icon(Icons.Outlined.Lock, null) },
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, null)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirmar Password
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
                        leadingIcon = { Icon(Icons.Outlined.Lock, null) },
                        trailingIcon = {
                            val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(imageVector = image, null)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dirección
                    OutlinedTextField(
                        value = uiState.direccion,
                        onValueChange = { viewModel.onRegistroEvent(RegistroEvent.OnDireccionChange(it)) },
                        label = { Text("Dirección (Opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Outlined.Home, null) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

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

                    uiState.error?.let { error ->
                        Text(text = error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                    }

                    TextButton(onClick = onVolverALogin) {
                        Text("¿Ya tienes cuenta? Inicia sesión")
                    }
                }
            }
        }
    }
}