package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.milsabores.di.AppContainer // <-- Import
import com.example.milsabores.data.local.crearDatosCompra // <-- Import

// Constantes
private const val COSTO_ENVIO = 3000.0
private const val ENVIO_GRATIS_MINIMO = 50000.0

class CheckoutViewModel(
    private val carritoRepository: CarritoRepository,
    private val productoRepository: ProductoRepository,
    private val appContainer: AppContainer // <-- Mantenemos esto
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    init {
        // (La lógica para cargar el total sigue igual y es correcta)
        viewModelScope.launch {
            productoRepository.obtenerTodos()
                .combine(carritoRepository.obtenerItems()) { productos, itemsCarrito ->
                    itemsCarrito.mapNotNull { item ->
                        productos.find { it.id == item.productoId }
                            ?.let { producto ->
                                ItemCarritoDetallado(producto, item.cantidad)
                            }
                    }
                }
                .collect { itemsDetallados ->
                    val subtotal = itemsDetallados.sumOf { it.producto.precio * it.cantidad.toDouble() }
                    val envio = if (subtotal >= ENVIO_GRATIS_MINIMO) 0.0 else COSTO_ENVIO
                    _uiState.update {
                        it.copy(
                            items = itemsDetallados,
                            subtotal = subtotal,
                            costoEnvio = envio,
                            totalAPagar = subtotal + envio
                        )
                    }
                }
        }
    }

    // --- Funciones para actualizar los campos (UDF) ---
    fun onFieldChange(campo: String, valor: String) {
        _uiState.update {
            when (campo) {
                // Entrega
                "nombre" -> it.copy(nombre = valor, errorNombre = null)
                "telefono" -> it.copy(telefono = valor, errorTelefono = null)
                "email" -> it.copy(email = valor, errorEmail = null)
                "calle" -> it.copy(calle = valor, errorCalle = null)
                "numeroCalle" -> it.copy(numeroCalle = valor, errorNumeroCalle = null)
                "comuna" -> it.copy(comuna = valor, errorComuna = null)
                "fechaEntrega" -> it.copy(fechaEntrega = valor, errorFechaEntrega = null)
                "comentarios" -> it.copy(comentarios = valor)
                // Pago
                "metodoPago" -> it.copy(metodoPago = valor, errorMetodoPago = null)
                else -> it
            }
        }
    }

    // --- Lógica de Validación (ACTUALIZADA) ---
    private fun validarFormulario(): Boolean {
        val estado = _uiState.value
        var esValido = true

        // 1. Validación de Entrega
        if (estado.nombre.isBlank()) {
            _uiState.update { it.copy(errorNombre = "Nombre no puede estar vacío") }; esValido = false
        }
        if (estado.email.isBlank() || !estado.email.contains("@")) {
            _uiState.update { it.copy(errorEmail = "Email inválido") }; esValido = false
        }
        if (estado.calle.isBlank()) {
            _uiState.update { it.copy(errorCalle = "La calle no puede estar vacía") }; esValido = false
        }
        if (estado.numeroCalle.isBlank()) {
            _uiState.update { it.copy(errorNumeroCalle = "El número no puede estar vacío") }; esValido = false
        }
        if (estado.comuna.isBlank()) {
            _uiState.update { it.copy(errorComuna = "Debe seleccionar una comuna") }; esValido = false
        }
        if (estado.fechaEntrega.isBlank()) {
            _uiState.update { it.copy(errorFechaEntrega = "Debe seleccionar una fecha") }; esValido = false
        }

        // 2. Validación de Pago (ACTUALIZADA)
        if (estado.metodoPago.isBlank()) {
            _uiState.update { it.copy(errorMetodoPago = "Debe seleccionar un método de pago") }; esValido = false
        }

        return esValido
    }

    fun onPagarClick() {
        if (validarFormulario()) {
            _uiState.update { it.copy(estaProcesando = true) }

            // Creamos el recibo con los campos actualizados
            val datosCompra = crearDatosCompra(_uiState.value)
            appContainer.ultimaCompra = datosCompra

            viewModelScope.launch {
                carritoRepository.limpiarCarrito()
                _uiState.update { it.copy(estaProcesando = false, pagoExitoso = true) }
            }
        }
    }
}
