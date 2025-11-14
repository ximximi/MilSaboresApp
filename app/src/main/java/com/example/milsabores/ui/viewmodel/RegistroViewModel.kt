package com.example.milsabores.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.local.entity.Usuario
import com.example.milsabores.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 1. Estado de la UI (la "foto" de la pantalla de registro)
data class RegistroUiState(
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val direccion: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val registroExitoso: Boolean = false
)

// 2. Eventos de la UI (los "mensajes" de la pantalla al ViewModel)
sealed interface RegistroEvent {
    data class OnNombreChange(val nombre: String) : RegistroEvent
    data class OnEmailChange(val email: String) : RegistroEvent
    data class OnPasswordChange(val pass: String) : RegistroEvent
    data class OnConfirmarPasswordChange(val pass: String) : RegistroEvent
    data class OnDireccionChange(val direccion: String) : RegistroEvent
    object OnRegistroClick : RegistroEvent
}

// 3. El ViewModel (El "Cerebro")
class RegistroViewModel(
    private val usuarioRepository: UsuarioRepository // <-- Pide el "contrato"
) : ViewModel() {

    // El estado privado que este ViewModel controla
    private val _uiState = MutableStateFlow(RegistroUiState())
    // El estado público que la UI observa (solo lectura)
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    /**
     * Única puerta de entrada para los eventos de la UI.
     */
    fun onRegistroEvent(event: RegistroEvent) {
        when (event) {
            is RegistroEvent.OnNombreChange -> _uiState.update { it.copy(nombre = event.nombre) }
            is RegistroEvent.OnEmailChange -> _uiState.update { it.copy(email = event.email) }
            is RegistroEvent.OnPasswordChange -> _uiState.update { it.copy(password = event.pass) }
            is RegistroEvent.OnConfirmarPasswordChange -> _uiState.update { it.copy(confirmarPassword = event.pass) }
            is RegistroEvent.OnDireccionChange -> _uiState.update { it.copy(direccion = event.direccion) }
            RegistroEvent.OnRegistroClick -> {
                intentarRegistro()
            }
        }
    }

    /**
     * Lógica de negocio para el registro (privada).
     */
    private fun intentarRegistro() {
        // Obtenemos los valores actuales del estado
        val state = uiState.value

        // 1. Revisar campos vacíos (excepto dirección)
        if (state.nombre.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "Nombre, Email y Contraseña son obligatorios") }
            return
        }
        // 2. Revisar formato de email
        if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.update { it.copy(error = "El formato del email no es válido") }
            return
        }
        // 3. Revisar que las contraseñas coincidan
        if (state.password != state.confirmarPassword) {
            _uiState.update { it.copy(error = "Las contraseñas no coinciden") }
            return
        }

        // Si todas las validaciones pasan, lanzamos la corrutina
        viewModelScope.launch {
            // Ponemos la UI en modo "Cargando"
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Creamos el objeto Usuario para insertar en la BD
            val nuevoUsuario = Usuario(
                nombre = state.nombre.trim(),
                email = state.email.trim(),
                password = state.password,
                direccion = state.direccion.trim().takeIf { it.isNotBlank() } // null si está vacía
            )

            try {
                // Llamamos al Repositorio (el "trabajador")
                val resultado = usuarioRepository.registrarUsuario(nuevoUsuario)

                if (resultado.isSuccess) {
                    // ¡Éxito!
                    _uiState.update { it.copy(isLoading = false, registroExitoso = true) }
                } else {
                    // Falla (Ej. email ya existe, lo maneja el Repo)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = resultado.exceptionOrNull()?.message ?: "Error al registrar"
                        )
                    }
                }
            } catch (e: Exception) {
                // Falla (Error de sistema, ej. BD).
                _uiState.update { it.copy(isLoading = false, error = "Error: ${e.message}") }
            }
        }
    }
}