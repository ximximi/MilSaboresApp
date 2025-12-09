package com.example.milsabores.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milsabores.data.local.ClienteInfo
import com.example.milsabores.data.local.DatosCompra
import com.example.milsabores.data.local.TotalesInfo
import com.example.milsabores.ui.viewmodel.AppViewModelProvider
import com.example.milsabores.ui.viewmodel.ConfirmacionViewModel
import com.example.milsabores.ui.viewmodel.ItemCarritoDetallado

@Composable
fun ConfirmacionScreen(
    onVolverAlInicioClick: () -> Unit,
    onSeguirComprandoClick: () -> Unit,
    viewModel: ConfirmacionViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        // No hay TopAppBar, es una pantalla final
    ) { paddingValues ->

        // Usamos un when para manejar los dos estados: Éxito o Error
        when {
            // --- Caso 1: ERROR (traducción de tu 'mostrarError()') ---
            uiState.error != null -> {
                ErrorScreen(
                    mensaje = uiState.error!!,
                    onVolverAlInicioClick = onVolverAlInicioClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            // --- Caso 2: ÉXITO (traducción de 'success-header', 'order-details', etc.) ---
            uiState.datosCompra != null -> {
                val datos = uiState.datosCompra!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // --- Header de Éxito ---
                    SuccessHeader(datos = datos)

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Detalles del Pedido ---
                    OrderDetails(datos = datos)

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Resumen de Pago ---
                    PaymentSummary(totales = datos.totales)

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- Botones de Acción ---
                    ConfirmationActions(
                        onVolverAlInicioClick = onVolverAlInicioClick,
                        onSeguirComprandoClick = onSeguirComprandoClick
                    )
                }
            }
        }
    }
}

// --- Componentes Privados para organizar la pantalla ---

@Composable
private fun SuccessHeader(datos: DatosCompra) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "Éxito",
            tint = MaterialTheme.colorScheme.primary, // O un color verde
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "¡Compra Aprobada!",
            style = MaterialTheme.typography.headlineLarge,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily
        )
        Text(
            text = "Pedido N°: ${datos.numeroPedido}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = datos.fecha,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun OrderDetails(datos: DatosCompra) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Productos Pedidos", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Lista de productos
            datos.productos.forEach { item ->
                SummaryProductItem(item = item) // Reutilizamos el item del Checkout
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // Info de entrega
            Text("Información de Entrega", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            InfoEntregaRow("Nombre:", datos.cliente.nombre)
            InfoEntregaRow("Email:", datos.cliente.email)
            InfoEntregaRow("Dirección:", "${datos.cliente.calle} ${datos.cliente.numeroCalle}") // Unimos calle y número
            InfoEntregaRow("Comuna:", datos.cliente.comuna)

            Spacer(modifier = Modifier.height(16.dp))

            // Info de Boleta
            Text("Hemos enviado la boleta a ${datos.cliente.email}", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun PaymentSummary(totales: TotalesInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background // Fondo Crema
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Resumen de Pago", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            ResumenLinea(texto = "Subtotal", valor = "$${totales.subtotal}")
            ResumenLinea(texto = "Envío", valor = "$${totales.envio}")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ResumenLinea(texto = "Total Pagado", valor = "$${totales.total}", esTotal = true)
        }
    }
}

@Composable
private fun ConfirmationActions(
    onVolverAlInicioClick: () -> Unit,
    onSeguirComprandoClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onSeguirComprandoClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.ShoppingCart, "Seguir Comprando", modifier = Modifier.padding(end = 8.dp))
            Text("SEGUIR COMPRANDO")
        }
        OutlinedButton(
            onClick = onVolverAlInicioClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Home, "Volver al Inicio", modifier = Modifier.padding(end = 8.dp))
            Text("VOLVER AL INICIO")
        }
    }
}

@Composable
private fun InfoEntregaRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.width(100.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ErrorScreen(
    mensaje: String,
    onVolverAlInicioClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Error en el Pedido",
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily
        )
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onVolverAlInicioClick) {
            Icon(Icons.Default.Home, "Volver al Inicio", modifier = Modifier.padding(end = 8.dp))
            Text("VOLVER AL INICIO")
        }
    }
}

/**
 * ¡NUEVO! Componente para mostrar cada producto en el resumen
 * (Copiado de CheckoutScreen, podríamos moverlo a 'components' después)
 */
@Composable
private fun SummaryProductItem(item: ItemCarritoDetallado) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/${item.producto.imagen}")
                .crossfade(true)
                .build(),
            contentDescription = item.producto.nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .padding(end = 8.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.producto.nombre,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Cantidad: ${item.cantidad}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = "$${item.producto.precio * item.cantidad}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Componente reutilizable para las líneas del resumen (Subtotal, Total, etc.)
 */
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