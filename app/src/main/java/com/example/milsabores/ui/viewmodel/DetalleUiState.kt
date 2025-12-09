package com.example.milsabores.ui.viewmodel

import com.example.milsabores.data.local.entity.Producto

data class DetalleUiState(
    val estaCargando: Boolean = true,
    val producto: Producto? = null, // Puede ser nulo al inicio
    val error: String? = null
)