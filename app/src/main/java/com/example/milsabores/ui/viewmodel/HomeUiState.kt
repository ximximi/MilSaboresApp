package com.example.milsabores.ui.viewmodel

import com.example.milsabores.data.local.entity.Blog
import com.example.milsabores.data.local.entity.Producto

/**
 * Representa todos los estados posibles de la HomeScreen.
 */
data class HomeUiState(
    val estaCargando: Boolean = true,
    val productos: List<Producto> = emptyList(),
    val entradasBlog: List<Blog> = emptyList(),
    val error: String? = null
)