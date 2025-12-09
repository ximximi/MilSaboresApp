package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.BlogRepository
import com.example.milsabores.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * El cerebro de la HomeScreen.
 * Pide al Repositorio los datos y los prepara para la Vista.
 */
class HomeViewModel(
    private val productoRepository: ProductoRepository, // Pide el Repositorio
    private val blogRepository: BlogRepository
) : ViewModel() {

    // _uiState es privado y "mutable" (puede cambiar)
    private val _uiState = MutableStateFlow(HomeUiState())
    // uiState es público e "inmutable" (solo se puede leer)
    // La pantalla (View) observará este 'state'.
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // El bloque 'init' se ejecuta tan pronto como se crea el ViewModel
    init {
        // Vamos a cargar ambos flujos de datos a la vez
        obtenerDatosCombinados()
    }

    private fun obtenerDatosCombinados() {
        viewModelScope.launch {
            // 'combine' une los dos flujos. Solo emitirá cuando AMBOS tengan datos.
            productoRepository.obtenerTodos()
                .combine(blogRepository.obtenerTodos()) { productos, blogs ->
                    // Creamos un par de datos
                    Pair(productos, blogs)
                }
                .onStart { _uiState.update { it.copy(estaCargando = true) } }
                .catch { e -> _uiState.update { it.copy(estaCargando = false, error = e.message) } }
                .collect { (productos, blogs) ->
                    _uiState.update {
                        it.copy(
                            estaCargando = false,
                            productos = productos,
                            entradasBlog = blogs
                        )
                    }
                }
        }
    }
}