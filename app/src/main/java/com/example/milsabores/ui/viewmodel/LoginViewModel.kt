package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 1. Estado de la UI (la "foto" de la pantalla)
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginExitoso: Boolean = false
)

// 2. Eventos de la UI (los "mensajes" de la pantalla al ViewModel)
sealed interface LoginEvent {
    data class OnEmailChange(val email: String) : LoginEvent
    data class OnPasswordChange(val pass: String) : LoginEvent
    object OnLoginClick : LoginEvent
}

// 3. El ViewModel (El "Cerebro")
class LoginViewModel(
    private val usuarioRepository: UsuarioRepository // <-- Pide el "contrato"
) : ViewModel() {

    // El estado privado que este ViewModel controla
    private val _uiState = MutableStateFlow(LoginUiState())
    // El estado público que la UI observa (solo lectura)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Única puerta de entrada para los eventos de la UI.
     */
    fun onLoginEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> {
                _uiState.update { it.copy(email = event.email) }
            }
            is LoginEvent.OnPasswordChange -> {
                _uiState.update { it.copy(password = event.pass) }
            }
            LoginEvent.OnLoginClick -> {
                intentarLogin()
            }
        }
    }

    /**
     * Lógica de negocio para el login (privada).
     */
    private fun intentarLogin() {
        // --- VALIDACIÓN (Rúbrica IE 2.2.1) ---
        if (uiState.value.email.isBlank() || uiState.value.password.isBlank()) {
            _uiState.update { it.copy(error = "Email y contraseña no pueden estar vacíos") }
            return
        }
        // TODO: (Opcional) Añadir validación de formato de email aquí

        // Lanzamos la corrutina para la llamada a la BD
        viewModelScope.launch {
            // Ponemos la UI en modo "Cargando"
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Llamamos al Repositorio (el "trabajador")
                val usuario = usuarioRepository.login(
                    uiState.value.email,
                    uiState.value.password
                )

                if (usuario != null) {
                    // ¡Éxito!
                    _uiState.update { it.copy(isLoading = false, loginExitoso = true) }
                } else {
                    // Falla (Credenciales)
                    _uiState.update { it.copy(isLoading = false, error = "Credenciales incorrectas") }
                }
            } catch (e: Exception) {
                // Falla (Error de sistema, ej. BD)
                _uiState.update { it.copy(isLoading = false, error = "Error: ${e.message}") }
            }
        }
    }
}