package com.example.milsabores.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
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
    onProductoClick: (Int) -> Unit,
    onPerfilClick: () -> Unit,
    viewModel: CatalogoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Obtenemos el estado del ViewModel (cargando, productos, filtros, etc.)
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestro Catálogo") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onPerfilClick) {
                        Icon(Icons.Filled.AccountCircle, "Mi Perfil")
                    }
                    IconButton(onClick = onCarritoClick) {
                        Icon(Icons.Filled.ShoppingCart, "Carrito")
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

        Column(Modifier.padding(paddingValues)) {

            // --- SECCIÓN 1: BARRA DE BÚSQUEDA ---
            OutlinedTextField(
                value = uiState.busqueda,
                onValueChange = { viewModel.actualizarBusqueda(it) }, // ¡Conectado al ViewModel!
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar pastel...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (uiState.busqueda.isNotEmpty()) {
                        IconButton(onClick = { viewModel.actualizarBusqueda("") }) {
                            Icon(Icons.Default.Close, "Borrar búsqueda")
                        }
                    }
                },
                singleLine = true
            )

            // --- SECCIÓN 2: FILTROS (Categoría y Precio) ---
            Column(Modifier.padding(horizontal = 16.dp)) {

                // A. Filtros de Categoría (LazyRow)
                Text("Categorías:", style = MaterialTheme.typography.labelMedium)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(uiState.categorias) { categoria ->
                        FilterChip(
                            selected = categoria == uiState.categoriaSeleccionada,
                            // AQUÍ ESTABA EL ERROR: Cambiamos filtrarPorCategoria -> actualizarCategoria
                            onClick = { viewModel.actualizarCategoria(categoria) },
                            label = { Text(formatearNomeCategoria(categoria)) }
                        )
                    }
                }

                // B. Filtros de Orden (Precio) - AHORA CON SCROLL (LazyRow)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp) // Un poco de aire arriba y abajo
                ) {
                    // Elemento 1: La Etiqueta
                    item {
                        Text("Ordenar por precio:", style = MaterialTheme.typography.labelMedium)
                    }

                    // Elemento 2: Botón Barato
                    item {
                        FilterChip(
                            selected = uiState.ordenAscendente == true,
                            onClick = { viewModel.actualizarOrden(true) },
                            label = { Text("Menor") },
                            leadingIcon = { Icon(Icons.Default.KeyboardArrowUp, null) }
                        )
                    }

                    // Elemento 3: Botón Caro (Mayor)
                    item {
                        FilterChip(
                            selected = uiState.ordenAscendente == false,
                            onClick = { viewModel.actualizarOrden(false) },
                            label = { Text("Mayor") },
                            leadingIcon = { Icon(Icons.Default.KeyboardArrowDown, null) }
                        )
                    }

                    // Elemento 4: Botón Limpiar (Solo aparece si hay orden)
                    if (uiState.ordenAscendente != null) {
                        item {
                            IconButton(onClick = { viewModel.actualizarOrden(null) }) {
                                Icon(Icons.Default.Close, "Quitar orden")
                            }
                        }
                    }
                }
            }

            Divider(Modifier.padding(vertical = 8.dp))

            // --- SECCIÓN 3: RESULTADOS ---
            if (uiState.estaCargando) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error de conexión: ${uiState.error}")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (uiState.productos.isEmpty()) {
                        item {
                            Text(
                                "No encontramos productos con esos filtros :(",
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        items(items = uiState.productos, key = { it.id }) { producto ->
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
    return if (slug == "todos") "Todos" else slug.split('-')
        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}