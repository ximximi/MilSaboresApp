package com.example.milsabores.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milsabores.ui.components.CameraPreview
import com.example.milsabores.ui.viewmodel.AppViewModelProvider
import com.example.milsabores.ui.viewmodel.PerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    onVolverClick: () -> Unit,
    onCerrarSesion: () -> Unit,
    viewModel: PerfilViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val usuario by viewModel.usuarioLogueado.collectAsState()
    val context = LocalContext.current

    // Estado para controlar si mostramos la cámara
    var mostrarCamara by remember { mutableStateOf(false) }

    if (mostrarCamara) {
        // --- MODO CÁMARA ---
        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreview(
                onImageCaptured = { uri ->
                    // 1. Guardamos la foto en la BD
                    viewModel.actualizarFoto(uri.toString())
                    // 2. Cerramos la cámara
                    mostrarCamara = false
                    Toast.makeText(context, "Foto actualizada", Toast.LENGTH_SHORT).show()
                },
                onError = {
                    Toast.makeText(context, "Error al tomar foto", Toast.LENGTH_SHORT).show()
                    mostrarCamara = false
                }
            )
            // Botón Cancelar
            Button(
                onClick = { mostrarCamara = false },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text("Cancelar")
            }
        }
    } else {
        // --- MODO PERFIL (Normal) ---
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mi Perfil") },
                    navigationIcon = {
                        IconButton(onClick = onVolverClick) {
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (usuario != null) {
                    // --- FOTO DE PERFIL (Clickeable) ---
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier
                            .size(160.dp)
                            .clickable { mostrarCamara = true } // ¡Abre la cámara!
                    ) {
                        val fotoUri = usuario!!.fotoPerfil

                        // La Imagen
                        if (fotoUri != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(Uri.parse(fotoUri))
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Foto Perfil",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Placeholder
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                                    .padding(16.dp),
                                tint = Color.White
                            )
                        }

                        // Icono de "Cámara" pequeño encima (indicador visual)
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Cambiar foto",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(8.dp)
                                .align(Alignment.BottomEnd)
                        )
                    }

                    Text(
                        "Toca para cambiar foto",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- DATOS ---
                    Text(usuario!!.nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(usuario!!.email, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)

                    Spacer(modifier = Modifier.height(32.dp))

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Dirección de envío registrada:", fontWeight = FontWeight.Bold)
                            Text(usuario!!.direccion ?: "No registrada")
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // --- CERRAR SESIÓN ---
                    Button(
                        onClick = {
                            viewModel.cerrarSesion()
                            onCerrarSesion()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("CERRAR SESIÓN", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text("No hay usuario activo")
                    Button(onClick = onCerrarSesion) { Text("Ir al Login") }
                }
            }
        }
    }
}