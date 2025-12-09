package com.example.milsabores.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.milsabores.ui.components.CarritoItemCard
import com.example.milsabores.ui.viewmodel.AppViewModelProvider
import com.example.milsabores.ui.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    onVolverClick: () -> Unit,
    onIrAPagarClick: () -> Unit,
    onPerfilClick: () -> Unit,
    viewModel: CarritoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito", fontFamily = MaterialTheme.typography.titleLarge.fontFamily) },
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
                        IconButton(onClick = { viewModel.limpiarCarrito() }) {
                            Icon(Icons.Filled.DeleteSweep, "Limpiar Carrito")
                        }
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

        when {
            uiState.estaCargando -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text("Error: ${uiState.error}")
                }
            }
            // --- Caso: Carrito Vacío (tu <div id="carrito-vacio">) ---
            uiState.items.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues).padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Tu carrito está vacío",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("¡Agrega algunos de nuestros deliciosos productos!")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onVolverClick) {
                            Text("Ir al Catálogo")
                        }
                    }
                }
            }
            // --- Caso: Carrito con Productos ---
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // 1. La Lista de Productos
                    LazyColumn(
                        modifier = Modifier.weight(1f) // Ocupa el espacio menos el resumen
                    ) {
                        items(
                            items = uiState.items,
                            key = { item -> item.producto.id }
                        ) { item ->
                            CarritoItemCard(
                                item = item,
                                onSumar = { viewModel.cambiarCantidad(item.producto.id, item.cantidad + 1) },
                                onRestar = { viewModel.cambiarCantidad(item.producto.id, item.cantidad - 1) },
                                onEliminar = { viewModel.eliminarProducto(item.producto.id) }
                            )
                        }
                    }

                    // 2. El Resumen del Pedido
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Resumen del Pedido", style = MaterialTheme.typography.titleLarge)

                            ResumenLinea(texto = "Subtotal", valor = "$${uiState.subtotal}")
                            ResumenLinea(texto = "Envío", valor = "$${uiState.costoEnvio}")
                            if (uiState.montoDescuento > 0) {
                                ResumenLinea(texto = "Descuento", valor = "-$${uiState.montoDescuento}")
                            }
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                            ResumenLinea(
                                texto = "Total",
                                valor = "$${uiState.total}",
                                esTotal = true
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = onIrAPagarClick,
                                modifier = Modifier.fillMaxWidth().height(56.dp)
                            ) {
                                Icon(Icons.Filled.CreditCard, "Pagar", modifier = Modifier.padding(end = 8.dp))
                                Text("PROCEDER AL PAGO")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResumenLinea(texto: String, valor: String, esTotal: Boolean = false) {
    val estilo = if (esTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
    val peso = if (esTotal) FontWeight.Bold else FontWeight.Normal

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(texto, style = estilo, fontWeight = peso)
        Text(valor, style = estilo, fontWeight = peso)
    }
}