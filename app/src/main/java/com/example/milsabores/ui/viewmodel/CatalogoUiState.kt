package com.example.milsabores.ui.viewmodel

import com.example.milsabores.data.local.entity.Producto

data class CatalogoUiState(
    val estaCargando: Boolean = true,
    val productos: List<Producto> = emptyList(),
    val categorias: List<String> = listOf("todos", "tortas-cuadradas", "tortas-circulares", "vegana", "sin-azucar", "sin-gluten", "postres-individuales", "tradicional", "especiales" ), // Ya las dejamos fijas

    // --- NUEVOS CAMPOS DE ESTADO ---
    val categoriaSeleccionada: String = "todos",
    val busqueda: String = "",           // Lo que el usuario escribe en la barra
    val ordenAscendente: Boolean? = null, // null = sin orden, true = barato a caro, false = caro a barato

    val error: String? = null
)