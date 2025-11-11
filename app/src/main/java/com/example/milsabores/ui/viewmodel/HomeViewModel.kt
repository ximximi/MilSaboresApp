package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * El cerebro de la HomeScreen.
 * Pide al Repositorio los datos y los prepara para la Vista.
 */
class HomeViewModel(
    private val productoRepository: ProductoRepository // Pide el Repositorio
) : ViewModel() {

    // _uiState es privado y "mutable" (puede cambiar)
    private val _uiState = MutableStateFlow(HomeUiState())
    // uiState es público e "inmutable" (solo se puede leer)
    // La pantalla (View) observará este 'state'.
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // El bloque 'init' se ejecuta tan pronto como se crea el ViewModel
    init {
        obtenerProductos()
    }

    private fun obtenerProductos() {
        // viewModelScope lanza una corutina (hilo secundario) segura
        viewModelScope.launch {
            productoRepository.obtenerTodos() // Llama al Repositorio
                .onStart {
                    // Antes de empezar, actualiza el estado a "cargando"
                    _uiState.update { it.copy(estaCargando = true, error = null) }
                }
                .catch { e ->
                    // Si hay un error, actualiza el estado con el error
                    _uiState.update { it.copy(estaCargando = false, error = e.message) }
                }
                .collect { productos ->
                    // Si todo va bien, actualiza el estado con la lista de productos
                    _uiState.update {
                        it.copy(estaCargando = false, productos = productos)
                    }
                }
        }
    }
}