package com.example.milsabores.ui.viewmodel

import com.example.milsabores.data.local.DatosCompra

/**
 * Representa el estado de la pantalla de Confirmación.
 */
data class ConfirmacionUiState(
    val datosCompra: DatosCompra? = null,
    val error: String? = null // Para tu 'mostrarError()'
)