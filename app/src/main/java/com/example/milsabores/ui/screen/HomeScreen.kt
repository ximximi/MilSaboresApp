package com.example.milsabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.DateRange
import com.example.milsabores.ui.components.FeatureCard
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milsabores.ui.theme.MilSaboresTheme
import androidx.compose.ui.res.painterResource
import com.example.milsabores.R
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.milsabores.ui.viewmodel.AppViewModelProvider
import com.example.milsabores.ui.viewmodel.HomeViewModel
import com.example.milsabores.ui.components.ProductoCard
import com.example.milsabores.ui.components.CategoriaCard
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onVolverClick: () -> Unit,
    onCarritoClick: () -> Unit,
    onVerCatalogoClick: () -> Unit,
    // 1. Pedimos el ViewModel. Usamos la Factory que creamos.
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // 2. Obtenemos el "estado" (UiState) del ViewModel
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        // --- 1. BARRA SUPERIOR ---
        topBar = {
            TopAppBar(
                title = { Text("Mil Sabores", fontFamily = MaterialTheme.typography.titleLarge.fontFamily) },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onCarritoClick) {
                        Icon(Icons.Default.ShoppingCart, "Carrito")
                    }
                },
                // Hacemos que la barra superior use los colores del tema
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Rosa
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, // Café
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->

        // --- 2. CONTENIDO PRINCIPAL (Traducción de tu <main>) ---
        // Usamos LazyColumn para que sea una lista scrolleable
        if (uiState.estaCargando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        // Si hay un error, muestra el error
        else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Error al cargar datos: ${uiState.error}")
            }
        }
        // Si está bien (no hay carga ni error), muestra la lista
        else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                // --- ITEM 1: El Banner ---
                item {
                    BannerSection(onVerCatalogoClick = onVerCatalogoClick)
                }

                // --- ITEM 2: Features (Placeholder) ---
                item {
                    Text(
                        text = "Nuestras Promesas",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre items
                    ) {
                        // Traducción de tu <div class="card-feature">
                        item {
                            FeatureCard(
                                icono = Icons.Default.ThumbUp, // (Cambiamos el ícono de FontAwesome)
                                titulo = "Garantía de frescura",
                                descripcion = "100% productos frescos"
                            )
                        }
                        item {
                            FeatureCard(
                                icono = Icons.Default.Info, // (Ícono de ejemplo)
                                titulo = "Personalización",
                                descripcion = "Diseños únicos"
                            )
                        }
                        item {
                            FeatureCard(
                                icono = Icons.Default.Phone, // (Ícono de ejemplo)
                                titulo = "Atención 24/7",
                                descripcion = "Contáctanos"
                            )
                        }
                    }
                }

                // --- ITEM 3: Categorías (Placeholder) ---
                item {
                    Text(
                        text = "Nuestras Especialidades",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        item {
                            CategoriaCard(
                                titulo = "Tortas",
                                imageName = "categoria_tortas.jpg", // ¡Usa el nombre de tu imagen!
                                onClick = onVerCatalogoClick // Reutilizamos la navegación al catálogo
                            )
                        }
                        item {
                            CategoriaCard(
                                titulo = "Postres",
                                imageName = "categoria_postres.jpg", // ¡Usa el nombre de tu imagen!
                                onClick = onVerCatalogoClick
                            )
                        }
                        item {
                            CategoriaCard(
                                titulo = "Especiales",
                                imageName = "categoria_especiales.jpg", // ¡Usa el nombre de tu imagen!
                                onClick = onVerCatalogoClick
                            )
                        }
                    }
                }

                // --- ITEM 4: Productos Destacados (¡DATOS REALES!) ---
                item {
                    Text(
                        text = "Productos Destacados",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )

                    // Si la lista de productos está vacía, muestra un mensaje
                    if (uiState.productos.isEmpty()) {
                        Text(
                            text = "No hay productos en la base de datos.",
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    } else {
                        LazyRow(
                            contentPadding = PaddingValues(start = 8.dp, end = 8.dp) // Ajustamos padding
                        ) {
                            // Usamos 'items' de esta forma para mejor rendimiento
                            items(
                                count = uiState.productos.size,
                                key = { index -> uiState.productos[index].id } // Key única para cada item
                            ) { index ->
                                val producto = uiState.productos[index]
                                // Llamamos a nuestro nuevo componente de tarjeta
                                ProductoCard(
                                    producto = producto,
                                    onProductoClick = {
                                        // Próximo paso: navegar a la pantalla de detalle
                                        // onProductoClick(producto.id)
                                    }
                                )
                            }
                        }
                    }
                }
            } // Fin del LazyColumn
        }
    }
}

// --- Componente Privado para el Banner ---
@Composable
private fun BannerSection(onVerCatalogoClick: () -> Unit) {
    // Box nos permite poner elementos uno encima de otro
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
    ) {
        // --- ESTE ES EL CAMBIO ---
        // Ya no usamos AsyncImage, usamos Image
        Image(
            // painterResource carga tu imagen desde /res/drawable
            painter = painterResource(id = R.drawable.banner_pasteleria),
            contentDescription = "Banner principal",
            contentScale = ContentScale.Crop, // Rellena el espacio
            modifier = Modifier.fillMaxSize()
        )
        // -------------------------

        // El contenido (tu <div class="content-banner">)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Sabores Únicos",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White // Forzamos color blanco para el banner
            )
            Button(onClick = onVerCatalogoClick) {
                Text("VER CATÁLOGO")
            }
        }
    }
}

// --- Preview para ver el diseño ---
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MilSaboresTheme {

        HomeScreen(
            onVolverClick = {},
            onCarritoClick = {},
            onVerCatalogoClick = {}
        )
    }
}