package com.example.milsabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.milsabores.R
import com.example.milsabores.ui.components.BlogCard
import com.example.milsabores.ui.components.CategoriaCard
import com.example.milsabores.ui.components.FeatureCard
import com.example.milsabores.ui.components.ProductoCard
import com.example.milsabores.ui.navigation.Rutas
import com.example.milsabores.ui.theme.MilSaboresTheme
import com.example.milsabores.ui.viewmodel.AppViewModelProvider
import com.example.milsabores.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    onVolverClick: () -> Unit,
    onCarritoClick: () -> Unit,
    onVerCatalogoClick: () -> Unit,
    onBlogClick: () -> Unit,
    onProductoClick: (Int) -> Unit, // <-- ¡Importante! Para navegar al detalle
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Rosa
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, // Café
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->

        // --- 2. CONTENIDO PRINCIPAL ---
        when {
            uiState.estaCargando -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error al cargar datos: ${uiState.error}")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                    // --- ITEM 1: El Banner ---
                    item {
                        BannerSection(onVerCatalogoClick = onVerCatalogoClick)
                    }

                    // --- ITEM 2: Features ---
                    item {
                        Text(
                            text = "Nuestras Promesas",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                FeatureCard(
                                    icono = Icons.Default.ThumbUp,
                                    titulo = "Garantía de frescura",
                                    descripcion = "100% productos frescos"
                                )
                            }
                            item {
                                FeatureCard(
                                    icono = Icons.Default.Info,
                                    titulo = "Personalización",
                                    descripcion = "Diseños únicos"
                                )
                            }
                            item {
                                FeatureCard(
                                    icono = Icons.Default.Phone,
                                    titulo = "Atención 24/7",
                                    descripcion = "Contáctanos"
                                )
                            }
                        }
                    }

                    // --- ITEM 3: Categorías ---
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
                                    imageName = "categoria_tortas.jpg",
                                    onClick = onVerCatalogoClick
                                )
                            }
                            item {
                                CategoriaCard(
                                    titulo = "Postres",
                                    imageName = "categoria_postres.jpg",
                                    onClick = onVerCatalogoClick
                                )
                            }
                            item {
                                CategoriaCard(
                                    titulo = "Especiales",
                                    imageName = "categoria_especiales.jpg",
                                    onClick = onVerCatalogoClick
                                )
                            }
                        }
                    }

                    // --- ITEM 4: Productos Destacados ---
                    item {
                        Text(
                            text = "Productos Destacados",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                        if (uiState.productos.isEmpty()) {
                            Text(
                                text = "No hay productos en la base de datos.",
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        } else {
                            LazyRow(
                                contentPadding = PaddingValues(start = 8.dp, end = 8.dp)
                            ) {
                                items(
                                    items = uiState.productos,
                                    key = { producto -> producto.id }
                                ) { producto ->
                                    ProductoCard(
                                        producto = producto,
                                        onProductoClick = { onProductoClick(producto.id) }
                                    )
                                }
                            }
                        }
                    }

                    // --- ITEM 5: ÚLTIMAS NOTICIAS ---
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Últimas Noticias",
                                style = MaterialTheme.typography.titleLarge
                            )
                            TextButton(onClick = onBlogClick) {
                                Text("Ver todo")
                            }
                        }
                    }

                    if (uiState.entradasBlog.isEmpty()) {
                        item {
                            Text(
                                "No hay noticias por el momento.",
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    } else {
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                            ) {
                                items(
                                    items = uiState.entradasBlog,
                                    key = { blog -> blog.id }
                                ) { blog ->
                                    Box(modifier = Modifier.width(300.dp)) {
                                        BlogCard(
                                            blog = blog,
                                            onClick = { navController.navigate(Rutas.irADetalleBlog(blog.id)) }                                         )
                                    }
                                }
                            }
                        }
                    }

                    // Espacio al final de la lista
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } // Fin del LazyColumn
            }
        }
    }
}

// --- Componente Privado para el Banner ---
@Composable
private fun BannerSection(onVerCatalogoClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner_pasteleria), // Carga desde drawable
            contentDescription = "Banner principal",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Sabores Únicos",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
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
            onVerCatalogoClick = {},
            onBlogClick = {},
            onProductoClick = {}
        )
    }
}