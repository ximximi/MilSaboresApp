package com.example.milsabores.ui.viewmodel

data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val loginExitoso: Boolean = false,
    val esAdmin: Boolean = false
)