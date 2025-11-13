package com.example.milsabores.ui.viewmodel

import com.example.milsabores.data.local.entity.Producto

data class CatalogoUiState(
    val estaCargando: Boolean = true,
    val productos: List<Producto> = emptyList(), // La lista de productos
    val categorias: List<String> = emptyList(), // Las categorías para los botones de filtro
    val categoriaSeleccionada: String = "todos", // Para saber qué filtro está activo
    val error: String? = null
)
