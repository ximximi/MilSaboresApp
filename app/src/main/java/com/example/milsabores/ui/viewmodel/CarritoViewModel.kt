package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.local.entity.ItemCarrito
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Constantes de tu carrito.js
private const val COSTO_ENVIO = 3000.0
private const val ENVIO_GRATIS_MINIMO = 50000.0

class CarritoViewModel(
    private val carritoRepository: CarritoRepository,
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())
    val uiState: StateFlow<CarritoUiState> = _uiState.asStateFlow()

    init {
        // "Combina" los dos flujos: el de productos y el de items del carrito
        viewModelScope.launch {
            productoRepository.obtenerTodos()
                .combine(carritoRepository.obtenerItems()) { productos, itemsCarrito ->
                    // Para cada item en el carrito, busca su detalle de producto
                    itemsCarrito.mapNotNull { item ->
                        productos.find { it.id == item.productoId }
                            ?.let { producto ->
                                // Crea la lista de items detallados
                                ItemCarritoDetallado(producto, item.cantidad)
                            }
                    }
                }
                .collect { itemsDetallados ->
                    // Cuando la lista combinada esté lista, actualiza el estado
                    _uiState.update { it.copy(
                        estaCargando = false,
                        items = itemsDetallados
                    ) }
                    // Y recalcula los totales
                    calcularTotales()
                }
        }
    }

    // --- Lógica de tu carrito.js ---

    private fun calcularTotales() {
        val items = _uiState.value.items
        val subtotal = items.sumOf { it.producto.precio * it.cantidad.toDouble() }
        val envio = if (subtotal >= ENVIO_GRATIS_MINIMO) 0.0 else COSTO_ENVIO
        val descuento = _uiState.value.montoDescuento

        _uiState.update {
            it.copy(
                subtotal = subtotal,
                costoEnvio = envio,
                total = subtotal + envio - descuento
            )
        }
    }

    fun aplicarCodigo(codigo: String) {
        val codigosValidos = mapOf(
            "DULCE10" to 0.10,
            "PRIMERA15" to 0.15,
            "CUMPLE20" to 0.20
        )

        val porcentaje = codigosValidos[codigo.uppercase()]
        if (porcentaje != null) {
            val subtotal = _uiState.value.subtotal
            val descuento = subtotal * porcentaje
            _uiState.update { it.copy(montoDescuento = descuento) }
        } else {
            // Código no válido, resetea el descuento
            _uiState.update { it.copy(montoDescuento = 0.0) }
        }
        calcularTotales() // Recalcula con el nuevo descuento
    }

    fun cambiarCantidad(productoId: Int, cantidadNueva: Int) {
        if (cantidadNueva <= 0) {
            eliminarProducto(productoId)
            return
        }
        viewModelScope.launch {
            // El DAO lo maneja como "insertar o reemplazar"
            carritoRepository.actualizarCantidad(
                productoId = productoId,
                nuevaCantidad = cantidadNueva
            )
        }
    }

    fun eliminarProducto(productoId: Int) {
        viewModelScope.launch {
            carritoRepository.eliminarDelCarrito(productoId)
        }
    }

    fun limpiarCarrito() {
        viewModelScope.launch {
            carritoRepository.limpiarCarrito()
        }
    }
}