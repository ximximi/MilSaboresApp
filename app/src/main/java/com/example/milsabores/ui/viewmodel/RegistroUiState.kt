package com.example.milsabores.ui.viewmodel

data class RegistroUiState(
    val nombre: String = "",
    val email: String = "",
    val pass: String = "",
    val repass: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val registroExitoso: Boolean = false
)