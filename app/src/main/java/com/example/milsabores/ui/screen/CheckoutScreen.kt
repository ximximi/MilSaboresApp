package com.example.milsabores.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milsabores.R
import com.example.milsabores.ui.viewmodel.AppViewModelProvider
import com.example.milsabores.ui.viewmodel.CheckoutViewModel
import com.example.milsabores.ui.viewmodel.ItemCarritoDetallado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onVolverClick: () -> Unit,
    onPagoExitoso: () -> Unit,
    viewModel: CheckoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.pagoExitoso) {
        if (uiState.pagoExitoso) {
            onPagoExitoso()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finalizar Compra", fontFamily = MaterialTheme.typography.titleLarge.fontFamily) },
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            // --- 1. FORMULARIO DE ENTREGA ---
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Información de Entrega", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                ValidatedTextField(
                    value = uiState.nombre,
                    onValueChange = { viewModel.onFieldChange("nombre", it) },
                    label = "Nombre completo *",
                    errorMessage = uiState.errorNombre
                )
                ValidatedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onFieldChange("email", it) },
                    label = "Correo electrónico *",
                    errorMessage = uiState.errorEmail,
                    keyboardType = KeyboardType.Email
                )
                ValidatedTextField(
                    value = uiState.telefono,
                    onValueChange = { viewModel.onFieldChange("telefono", it) },
                    label = "Teléfono *",
                    errorMessage = uiState.errorTelefono,
                    keyboardType = KeyboardType.Phone
                )
                ValidatedTextField(
                    value = uiState.direccion,
                    onValueChange = { viewModel.onFieldChange("direccion", it) },
                    label = "Dirección de entrega *",
                    errorMessage = uiState.errorDireccion
                )
            }

            Divider()

            // --- 2. FORMULARIO DE PAGO ---
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Información de Pago", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                ValidatedTextField(
                    value = uiState.nombreTitular,
                    onValueChange = { viewModel.onFieldChange("nombreTitular", it) },
                    label = "Nombre del titular *",
                    errorMessage = uiState.errorNombreTitular
                )
                ValidatedTextField(
                    value = uiState.numeroTarjeta,
                    onValueChange = { viewModel.onFieldChange("numeroTarjeta", it) },
                    label = "Número de tarjeta *",
                    errorMessage = uiState.errorNumeroTarjeta,
                    keyboardType = KeyboardType.Number
                )
                Row(Modifier.fillMaxWidth()) {
                    ValidatedTextField(
                        value = uiState.fechaVenc,
                        onValueChange = { viewModel.onFieldChange("fechaVenc", it) },
                        label = "MM/AA *",
                        errorMessage = uiState.errorFechaVenc,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    ValidatedTextField(
                        value = uiState.cvv,
                        onValueChange = { viewModel.onFieldChange("cvv", it) },
                        label = "CVV *",
                        errorMessage = uiState.errorCvv,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    )
                }
            }

            Divider()

            // --- 3. RESUMEN DEL PEDIDO (¡ACTUALIZADO!) ---
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Resumen del Pedido", style = MaterialTheme.typography.titleLarge)

                // --- ¡NUEVO! TRADUCCIÓN DE <div class="summary-products"> ---
                uiState.items.forEach { item ->
                    SummaryProductItem(item = item)
                }
                // --- FIN DE LA ACTUALIZACIÓN ---

                Divider(modifier = Modifier.padding(vertical = 4.dp))
                ResumenLinea(texto = "Subtotal", valor = "$${uiState.subtotal}")
                ResumenLinea(texto = "Envío", valor = "$${uiState.costoEnvio}")

                Divider(modifier = Modifier.padding(vertical = 4.dp))

                ResumenLinea(
                    texto = "Total",
                    valor = "$${uiState.totalAPagar}",
                    esTotal = true
                )
            }

            // --- 4. BOTÓN FINALIZAR COMPRA ---
            Button(
                onClick = { viewModel.onPagarClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                enabled = !uiState.estaProcesando
            ) {
                if (uiState.estaProcesando) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Icon(Icons.Filled.Lock, "Pagar", modifier = Modifier.padding(end = 8.dp))
                    Text("PAGAR $${uiState.totalAPagar}")
                }
            }
        }
    }
}

/**
 * Componente reutilizable para un campo de texto con validación.
 */
@Composable
private fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = errorMessage != null,
        supportingText = {
            if (errorMessage != null) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
    Spacer(modifier = Modifier.height(8.dp))
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

/**
 * ¡NUEVO! Componente para mostrar cada producto en el resumen
 * (Traducción de <div class="summary-product">)
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