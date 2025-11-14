package com.example.milsabores.ui.viewmodel

import com.example.milsabores.data.local.entity.Blog

data class DetalleBlogUiState(
    val estaCargando: Boolean = true,
    val entrada: Blog? = null,
    val error: String? = null
)