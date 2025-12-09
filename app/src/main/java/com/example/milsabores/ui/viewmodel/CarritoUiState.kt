package com.example.milsabores.ui.viewmodel

import com.example.milsabores.data.local.entity.Producto

/**
 * Representa el estado de la pantalla CarritoScreen.
 */
data class CarritoUiState(
    val estaCargando: Boolean = true,
    val items: List<ItemCarritoDetallado> = emptyList(), // La lista de items
    val error: String? = null,

    // Para el resumen del pedido
    val subtotal: Double = 0.0,
    val costoEnvio: Double = 3000.0,
    val montoDescuento: Double = 0.0,
    val total: Double = 0.0,

    val codigoDescuento: String = ""
)