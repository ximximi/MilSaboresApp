package com.example.milsabores.ui.viewmodel

import com.example.milsabores.data.local.entity.Blog

/**
 * Representa el estado de la pantalla del Blog.
 */
data class BlogUiState(
    val estaCargando: Boolean = true,
    val entradas: List<Blog> = emptyList(), // Lista de entradas del blog
    val error: String? = null
)