package com.example.milsabores.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.milsabores.ui.components.BlogCard
import com.example.milsabores.ui.navigation.Rutas
import com.example.milsabores.ui.viewmodel.AppViewModelProvider
import com.example.milsabores.ui.viewmodel.BlogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(
    navController: NavHostController,
    onVolverClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onCarritoClick: () -> Unit,
    viewModel: BlogViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Obtenemos el estado (cargando, entradas, error) del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Blog y Noticias", fontFamily = MaterialTheme.typography.titleLarge.fontFamily) },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    Row {
                        IconButton(onClick = onPerfilClick) {
                            Icon(Icons.Filled.AccountCircle, "Mi Perfil")
                        }
                        IconButton(onClick = onCarritoClick) {
                            Icon(Icons.Filled.ShoppingCart, "Carrito")
                        }
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
            // 3. ESTADO: ÉXITO
            else -> {
                // Usamos LazyColumn para una lista vertical scrolleable
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    if (uiState.entradas.isEmpty()) {
                        item {
                            Text("No hay entradas en el blog por el momento.", modifier = Modifier.padding(16.dp))
                        }
                    } else {
                        // Mostramos cada entrada del blog usando nuestra BlogCard
                        items(
                            items = uiState.entradas,
                            key = { blog -> blog.id }
                        ) { entrada ->
                            BlogCard(
                                blog = entrada,
                                onClick = { navController.navigate(Rutas.irADetalleBlog(entrada.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}