package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.milsabores.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ConfirmacionViewModel(
    private val appContainer: AppContainer // Pide el "localStorage"
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmacionUiState())
    val uiState: StateFlow<ConfirmacionUiState> = _uiState.asStateFlow()

    init {
        // Se ejecuta tan pronto como se crea el ViewModel
        cargarDatosDeLaCompra()
    }

    private fun cargarDatosDeLaCompra() {
        // 1. Lee los datos del "localStorage" (el AppContainer)
        val datos = appContainer.ultimaCompra

        if (datos == null) {
            // 2. Si no hay datos, muestra un error
            // (Esto pasa si el usuario entra a la pantalla sin pagar)
            _uiState.update {
                it.copy(error = "No se encontró información del pedido.")
            }
        } else {
            // 3. Si hay datos, los pone en el estado
            _uiState.update {
                it.copy(datosCompra = datos)
            }

            // 4. ¡Importante! Limpiamos los datos
            // (Igual que tu 'localStorage.removeItem')
            // Esto evita que el usuario pueda volver a ver esta pantalla.
            appContainer.ultimaCompra = null
        }
    }
}