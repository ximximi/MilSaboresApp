package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalleViewModel(
    savedStateHandle: SavedStateHandle, // 1. Para recibir el ID de la navegación
    private val productoRepository: ProductoRepository, // 2. Para buscar el producto
    private val carritoRepository: CarritoRepository  // 3. Para añadir al carrito
) : ViewModel() {

    // Obtenemos el "productoId" que pasamos en la ruta (ej. "detalle_producto/5")
    private val productoId: Int = checkNotNull(savedStateHandle["productoId"])

    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    init {
        obtenerProducto()
    }

    private fun obtenerProducto() {
        viewModelScope.launch {
            productoRepository.obtenerPorId(productoId) // Usamos la función del DAO
                .onStart { _uiState.update { it.copy(estaCargando = true) } }
                .catch { e -> _uiState.update { it.copy(estaCargando = false, error = e.message) } }
                .collect { producto ->
                    _uiState.update {
                        it.copy(estaCargando = false, producto = producto)
                    }
                }
        }
    }

    // Función que llamará el botón "Añadir al carrito"
    fun agregarAlCarrito() {
        viewModelScope.launch {
            val productoActual = _uiState.value.producto
            if (productoActual != null) {
                carritoRepository.agregarAlCarrito(productoActual, 1) // Añade 1 unidad
            }
        }
    }
}