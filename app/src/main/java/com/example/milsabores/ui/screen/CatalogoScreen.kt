package com.example.milsabores.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.milsabores.ui.components.ProductoCard
import com.example.milsabores.ui.viewmodel.AppViewModelProvider
import com.example.milsabores.ui.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    onVolverClick: () -> Unit,
    onCarritoClick: () -> Unit,
    onProductoClick: (Int) -> Unit, // Acción para ir al detalle
    viewModel: CatalogoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Obtenemos el estado del ViewModel (cargando, productos, filtros, etc.)
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestro Catálogo", fontFamily = MaterialTheme.typography.titleLarge.fontFamily) },
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
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->

        // Si está cargando, muestra la rueda
        if (uiState.estaCargando) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        // Si hay un error, muéstralo
        else if (uiState.error != null) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Error: ${uiState.error}")
            }
        }
        // Si todo va bien, muestra el contenido
        else {
            Column(Modifier.padding(paddingValues)) {

                // --- 1. LOS BOTONES DE FILTRO (<section class="filtros-section">) ---
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.categorias) { categoria ->
                        FilterChip(
                            selected = categoria == uiState.categoriaSeleccionada,
                            onClick = { viewModel.filtrarPorCategoria(categoria) },
                            label = { Text(formatearNomeCategoria(categoria)) }                        )
                    }
                }

                // --- 2. LA CUADRÍCULA DE PRODUCTOS (<div class="productos-grid">) ---
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp), // Cuadrícula adaptable (aprox. 2 columnas)
                    contentPadding = PaddingValues(8.dp)
                ) {
                    // Si no hay productos para ese filtro, muestra un mensaje
                    if (uiState.productos.isEmpty()) {
                        item {
                            Text(
                                "No se encontraron productos para esta categoría.",
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Si hay productos, los mostramos
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
        }
    }
}
private fun formatearNomeCategoria(slug: String): String {
    return slug.split('-') // Separa por guion: ["tortas", "cuadradas"]
        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    // Une con espacio y pone la primera letra de cada parte en mayúscula
}