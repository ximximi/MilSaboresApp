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

class CatalogoViewModel(
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogoUiState())
    val uiState: StateFlow<CatalogoUiState> = _uiState.asStateFlow()

    init {
        // Al iniciar, obtenemos TODOS los productos
        obtenerProductos(categoria = "todos")
    }

    fun filtrarPorCategoria(categoria: String) {
        // Actualizamos la categoría seleccionada y volvemos a cargar los productos
        _uiState.update { it.copy(categoriaSeleccionada = categoria) }
        obtenerProductos(categoria)
    }

    private fun obtenerProductos(categoria: String) {
        viewModelScope.launch {
            // Elige qué método del repositorio usar
            val flowProductos = if (categoria == "todos") {
                productoRepository.obtenerTodos()
            } else {
                productoRepository.obtenerPorCategoria(categoria)
            }

            flowProductos
                .onStart {
                    _uiState.update { it.copy(estaCargando = true, error = null) }
                }
                .catch { e ->
                    _uiState.update { it.copy(estaCargando = false, error = e.message) }
                }
                .collect { productos ->
                    // Cuando los productos lleguen, actualizamos el estado
                    _uiState.update {
                        it.copy(
                            estaCargando = false,
                            productos = productos,
                            // (Idealmente, las categorías vendrían de la BD también)
                            categorias = listOf("todos", "tortas-cuadradas", "tortas-circulares", "vegana", "sin-azucar")
                        )
                    }
                }
        }
    }
}
