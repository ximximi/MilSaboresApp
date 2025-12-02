package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.local.entity.Usuario
import com.example.milsabores.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerfilViewModel(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    // Observa al usuario logueado directamente del repositorio
    val usuarioLogueado: StateFlow<Usuario?> = usuarioRepository.usuarioLogueado
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    fun actualizarFoto(uriNueva: String) {
        val usuarioActual = usuarioLogueado.value
        if (usuarioActual != null) {
            viewModelScope.launch {
                usuarioRepository.actualizarFotoPerfil(usuarioActual.id, uriNueva)
            }
        }
    }
    fun cerrarSesion() {
        usuarioRepository.logout()
    }
}