package com.example.milsabores.ui.viewmodel

/**
 * Estado completo del formulario de Checkout, "traducido" de tu checkout.html.
 */
data class CheckoutUiState(
    // Datos del pedido
    val items: List<ItemCarritoDetallado> = emptyList(), // <-- ¡AÑADIDO!
    val totalAPagar: Double = 0.0,
    val subtotal: Double = 0.0,
    val costoEnvio: Double = 0.0,

    // 1. Campos de Entrega
    val nombre: String = "",
    val telefono: String = "",
    val email: String = "",
    val direccion: String = "",
    val comuna: String = "",
    val fechaEntrega: String = "",
    val comentarios: String = "",

    // 2. Campos de Pago
    val numeroTarjeta: String = "",
    val fechaVenc: String = "",
    val cvv: String = "",
    val nombreTitular: String = "",

    // 3. Estados de Error
    val errorNombre: String? = null,
    val errorTelefono: String? = null,
    val errorEmail: String? = null,
    val errorDireccion: String? = null,
    val errorComuna: String? = null,
    val errorFechaEntrega: String? = null,
    val errorNumeroTarjeta: String? = null,
    val errorFechaVenc: String? = null,
    val errorCvv: String? = null,
    val errorNombreTitular: String? = null,

    // 4. Estado general
    val estaProcesando: Boolean = false,
    val pagoExitoso: Boolean = false
)