package com.example.milsabores.ui.viewmodel

import com.example.milsabores.data.local.entity.Producto

/**
 * Representa todos los estados posibles de la HomeScreen.
 */
data class HomeUiState(
    val estaCargando: Boolean = true,
    val productos: List<Producto> = emptyList(), // La lista de productos
    val error: String? = null // Un mensaje de error si algo falla
)