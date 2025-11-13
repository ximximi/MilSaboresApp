package com.example.milsabores.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milsabores.R // Para el R.drawable
import com.example.milsabores.ui.viewmodel.AppViewModelProvider
import com.example.milsabores.ui.viewmodel.DetalleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    onVolverClick: () -> Unit,
    viewModel: DetalleViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Obtenemos el estado (cargando, producto, error) del ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current // Para mostrar el Toast

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
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

        // Manejamos los 3 estados posibles
        when {
            // 1. ESTADO: CARGANDO
            uiState.estaCargando -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            // 2. ESTADO: ERROR
            uiState.error != null -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text("Error: ${uiState.error}")
                }
            }
            // 3. ESTADO: ÉXITO (tenemos un producto)
            uiState.producto != null -> {
                val producto = uiState.producto!!

                // Usamos Column con scroll vertical para que quepa todo
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp) // Padding general
                        .verticalScroll(rememberScrollState()) // ¡Permite scrollear!
                ) {
                    // --- IMAGEN DEL PRODUCTO ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data("file:///android_asset/${producto.imagen}")
                                .crossfade(true)
                                .build(),
                            contentDescription = producto.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            // Placeholder por si la imagen no carga
                            error = painterResource(id = R.drawable.logo_mil_sabores)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- NOMBRE DEL PRODUCTO (tu <h1>) ---
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.headlineLarge,
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily // Fuente Pacifico
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- PRECIO DEL PRODUCTO (tu <p id="precio-producto">) ---
                    Text(
                        text = "$${producto.precio}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.secondary, // Color Café
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- DESCRIPCIÓN (tu <p id="descripcion-producto">) ---
                    Text(
                        text = producto.descripcion,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- DETALLES (Código y Categoría) ---
                    Text(
                        text = "Categoría: ${producto.categoria}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Código: ${producto.codigo}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.weight(1f)) // Empuja el botón al fondo

                    // --- BOTÓN "AÑADIR AL CARRITO" (tu <button>) ---
                    Button(
                        onClick = {
                            viewModel.agregarAlCarrito()
                            // Damos retroalimentación visual al usuario
                            Toast.makeText(context, "${producto.nombre} añadido al carrito", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                        Text("AGREGAR AL CARRITO")
                    }
                }
            }
        }
    }
}