package com.example.milsabores.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

                // --- CAMPOS DE DIRECCIÓN ACTUALIZADOS ---
                Row(Modifier.fillMaxWidth()) {
                    ValidatedTextField(
                        value = uiState.calle,
                        onValueChange = { viewModel.onFieldChange("calle", it) },
                        label = "Calle *",
                        errorMessage = uiState.errorCalle,
                        modifier = Modifier.weight(2f).padding(end = 8.dp)
                    )
                    ValidatedTextField(
                        value = uiState.numeroCalle,
                        onValueChange = { viewModel.onFieldChange("numeroCalle", it) },
                        label = "Número *",
                        errorMessage = uiState.errorNumeroCalle,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    )
                }

                // --- NUEVO: DROPDOWN DE COMUNA ---
                ComunaDropdown(
                    comunaSeleccionada = uiState.comuna,
                    comunas = uiState.listaComunas,
                    onComunaSelect = { viewModel.onFieldChange("comuna", it) },
                    errorMessage = uiState.errorComuna
                )

                // --- NUEVO: PICKER DE FECHA ---
                FechaEntregaPicker(
                    fechaSeleccionada = uiState.fechaEntrega,
                    onFechaSelect = { viewModel.onFieldChange("fechaEntrega", it) },
                    errorMessage = uiState.errorFechaEntrega
                )
            }

            Divider()

            // --- 2. FORMULARIO DE PAGO (SIMPLIFICADO) ---
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Información de Pago", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                // --- NUEVO: SELECTOR DE MÉTODO DE PAGO ---
                MetodoPagoSelector(
                    metodoSeleccionado = uiState.metodoPago,
                    onMetodoSelect = { viewModel.onFieldChange("metodoPago", it) },
                    errorMessage = uiState.errorMetodoPago
                )
            }

            Divider()

            // --- 3. RESUMEN DEL PEDIDO ---
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Resumen del Pedido", style = MaterialTheme.typography.titleLarge)
                uiState.items.forEach { item ->
                    SummaryProductItem(item = item)
                }
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
                // Habilitado solo si no está procesando
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

// --- Componentes Reutilizables (Helpers) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComunaDropdown(
    comunaSeleccionada: String,
    comunas: List<String>,
    onComunaSelect: (String) -> Unit,
    errorMessage: String?
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = comunaSeleccionada,
            onValueChange = {}, // No se puede cambiar escribiendo
            readOnly = true,
            label = { Text("Comuna *") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth().menuAnchor() // Importante
        )
        // Mensaje de error (si existe)
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            comunas.forEach { comuna ->
                DropdownMenuItem(
                    text = { Text(comuna) },
                    onClick = {
                        onComunaSelect(comuna)
                        expanded = false
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FechaEntregaPicker(
    fechaSeleccionada: String,
    onFechaSelect: (String) -> Unit,
    errorMessage: String?
) {
    val context = LocalContext.current
    val datePickerState = rememberDatePickerState(
        // Deshabilita fechas anteriores a mañana
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val hoy = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                return utcTimeMillis > hoy.timeInMillis
            }
        }
    )
    var mostrarDialog by remember { mutableStateOf(false) }

    if (mostrarDialog) {
        DatePickerDialog(
            onDismissRequest = { mostrarDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialog = false
                    // Convierte el milisegundo a una fecha legible
                    val fecha = datePickerState.selectedDateMillis?.let {
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        sdf.format(Date(it))
                    } ?: ""
                    onFechaSelect(fecha)
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialog = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // El campo de texto que abre el diálogo
    OutlinedTextField(
        value = fechaSeleccionada,
        onValueChange = {},
        readOnly = true,
        label = { Text("Fecha de entrega *") },
        isError = errorMessage != null,
        trailingIcon = {
            Icon(
                Icons.Default.CalendarToday,
                "Seleccionar fecha",
                Modifier.clickable { mostrarDialog = true }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { mostrarDialog = true }
    )
    if (errorMessage != null) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun MetodoPagoSelector(
    metodoSeleccionado: String,
    onMetodoSelect: (String) -> Unit,
    errorMessage: String?
) {
    val opcion = "webpay" // Solo tenemos una opción
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onMetodoSelect(opcion) }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = metodoSeleccionado == opcion,
                onClick = { onMetodoSelect(opcion) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pagar con Webpay (Simulado)", style = MaterialTheme.typography.bodyLarge)
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

// --- Componentes de Resumen (Sin cambios) ---

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
