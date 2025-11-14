package com.example.milsabores.data.local

import com.example.milsabores.ui.viewmodel.CheckoutUiState
import com.example.milsabores.ui.viewmodel.ItemCarritoDetallado
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * "Traducción" del objeto 'datosCompra' de tu checkout.js
 * Guarda una instantánea del pedido final.
 */
data class DatosCompra(
    val numeroPedido: String,
    val fecha: String,
    val productos: List<ItemCarritoDetallado>,
    val cliente: ClienteInfo,
    val totales: TotalesInfo
)

// --- AQUÍ ESTÁ EL PRIMER CAMBIO ---
data class ClienteInfo(
    val nombre: String,
    val email: String,
    val telefono: String,
    val calle: String, // <-- ACTUALIZADO
    val numeroCalle: String, // <-- ACTUALIZADO
    val comuna: String,
    val fechaEntrega: String
)

data class TotalesInfo(
    val subtotal: Double,
    val envio: Double,
    val total: Double
)

/**
 * "Traducción" de tu 'generarNumeroPedido()' de checkout.js
 */
fun generarNumeroPedido(): String {
    val fecha = SimpleDateFormat("yyMMdd", Locale.getDefault()).format(Date())
    val random = (1000..9999).random()
    return "MS$fecha$random"
}

/**
 * Función "helper" para convertir el estado del checkout en un recibo final.
 */
fun crearDatosCompra(uiState: CheckoutUiState): DatosCompra {
    return DatosCompra(
        numeroPedido = generarNumeroPedido(),
        fecha = SimpleDateFormat("dd 'de' MMMM, yyyy - HH:mm", Locale.getDefault()).format(Date()),
        productos = uiState.items,

        // --- AQUÍ ESTÁ EL SEGUNDO CAMBIO ---
        cliente = ClienteInfo(
            nombre = uiState.nombre,
            email = uiState.email,
            telefono = uiState.telefono,
            calle = uiState.calle, // <-- ACTUALIZADO
            numeroCalle = uiState.numeroCalle, // <-- ACTUALIZADO
            comuna = uiState.comuna,
            fechaEntrega = uiState.fechaEntrega
        ),
        // ---------------------------------

        totales = TotalesInfo(
            subtotal = uiState.subtotal,
            envio = uiState.costoEnvio,
            total = uiState.totalAPagar
        )
    )
}
