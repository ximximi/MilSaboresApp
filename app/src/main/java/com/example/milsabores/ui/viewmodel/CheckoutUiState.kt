package com.example.milsabores.ui.viewmodel

/**
 * Estado completo del formulario de Checkout, ACTUALIZADO.
 */
data class CheckoutUiState(
    // Datos del pedido
    val items: List<ItemCarritoDetallado> = emptyList(),
    val totalAPagar: Double = 0.0,
    val subtotal: Double = 0.0,
    val costoEnvio: Double = 0.0,

    // 1. Campos de Entrega (Actualizados)
    val nombre: String = "",
    val telefono: String = "",
    val email: String = "",
    val calle: String = "",
    val numeroCalle: String = "",
    val comuna: String = "",
    val fechaEntrega: String = "",
    val comentarios: String = "",
    val listaComunas: List<String> = listOf( // (para el dropdown)
        "Santiago", "Providencia", "Las Condes", "Vitacura", "Ñuñoa", "La Reina"
    ),

    // 2. Campo de Pago (Simplificado)
    val metodoPago: String = "", // "webpay" o ""

    // 3. Estados de Error (Actualizados)
    val errorNombre: String? = null,
    val errorTelefono: String? = null,
    val errorEmail: String? = null,
    val errorCalle: String? = null,
    val errorNumeroCalle: String? = null,
    val errorComuna: String? = null,
    val errorFechaEntrega: String? = null,
    val errorMetodoPago: String? = null,

    // 4. Estado general
    val estaProcesando: Boolean = false,
    val pagoExitoso: Boolean = false
)