package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.di.AppContainer
import com.example.milsabores.data.local.crearDatosCompra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

private const val COSTO_ENVIO = 3000.0
private const val ENVIO_GRATIS_MINIMO = 50000.0

class CheckoutViewModel(
    private val carritoRepository: CarritoRepository,
    private val productoRepository: ProductoRepository,
    private val appContainer: AppContainer
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    init {
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
                            items = itemsDetallados, // <-- ¡AQUÍ ESTÁ EL CAMBIO!
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
                "nombre" -> it.copy(nombre = valor, errorNombre = null)
                "telefono" -> it.copy(telefono = valor, errorTelefono = null)
                "email" -> it.copy(email = valor, errorEmail = null)
                "direccion" -> it.copy(direccion = valor, errorDireccion = null)
                "comuna" -> it.copy(comuna = valor, errorComuna = null)
                "fechaEntrega" -> it.copy(fechaEntrega = valor, errorFechaEntrega = null)
                "comentarios" -> it.copy(comentarios = valor)
                "numeroTarjeta" -> it.copy(numeroTarjeta = valor, errorNumeroTarjeta = null)
                "fechaVenc" -> it.copy(fechaVenc = valor, errorFechaVenc = null)
                "cvv" -> it.copy(cvv = valor, errorCvv = null)
                "nombreTitular" -> it.copy(nombreTitular = valor, errorNombreTitular = null)
                else -> it
            }
        }
    }

    // --- Lógica de Validación (Traducción de tu checkout.js) ---
    private fun validarFormulario(): Boolean {
        val estado = _uiState.value
        var esValido = true

        if (estado.nombre.isBlank()) {
            _uiState.update { it.copy(errorNombre = "Nombre no puede estar vacío") }; esValido = false
        }
        if (!estado.email.contains("@") || estado.email.length < 5) {
            _uiState.update { it.copy(errorEmail = "Email inválido") }; esValido = false
        }
        if (estado.direccion.length < 5) {
            _uiState.update { it.copy(errorDireccion = "Dirección muy corta") }; esValido = false
        }
        if (!validarLuhn(estado.numeroTarjeta.replace(" ", ""))) {
            _uiState.update { it.copy(errorNumeroTarjeta = "Número de tarjeta inválido") }; esValido = false
        }
        if (!validarFechaVencimiento(estado.fechaVenc)) {
            _uiState.update { it.copy(errorFechaVenc = "Fecha inválida o vencida") }; esValido = false
        }
        if (estado.cvv.length != 3) {
            _uiState.update { it.copy(errorCvv = "CVV debe tener 3 dígitos") }; esValido = false
        }
        if (estado.nombreTitular.length < 2) {
            _uiState.update { it.copy(errorNombreTitular = "Nombre del titular inválido") }; esValido = false
        }
        return esValido
    }

    fun onPagarClick() {
        if (validarFormulario()) {
            _uiState.update { it.copy(estaProcesando = true) }
            // 1. Genera y guarda los datos de la compra
            val datosCompra = crearDatosCompra(_uiState.value)
            appContainer.ultimaCompra = datosCompra
            // --------------------------
            viewModelScope.launch {
                // 2. Limpia el carrito
                carritoRepository.limpiarCarrito()
                // 3. Navega a la confirmación
                _uiState.update { it.copy(estaProcesando = false, pagoExitoso = true) }
            }
        }
    }
    // --- Funciones de Validación --

    private fun validarLuhn(numero: String): Boolean {
        if (numero.length != 16 || numero.any { !it.isDigit() }) return false
        var suma = 0
        var alternar = false
        for (i in numero.length - 1 downTo 0) {
            var digito = numero[i].toString().toInt()
            if (alternar) {
                digito *= 2
                if (digito > 9) {
                    digito = (digito % 10) + 1
                }
            }
            suma += digito
            alternar = !alternar
        }
        return suma % 10 == 0
    }

    private fun validarFechaVencimiento(fecha: String): Boolean {
        if (fecha.length != 5 || fecha[2] != '/') return false
        return try {
            val (mes, anio) = fecha.split("/")
            val mesNum = mes.toInt()
            val anioNum = "20$anio".toInt()
            if (mesNum < 1 || mesNum > 12) return false
            val fechaActual = Calendar.getInstance()
            val fechaTarjeta = Calendar.getInstance()
            fechaTarjeta.set(anioNum, mesNum - 1, 1)
            fechaTarjeta.set(Calendar.DAY_OF_MONTH, fechaTarjeta.getActualMaximum(Calendar.DAY_OF_MONTH))
            fechaTarjeta > fechaActual
        } catch (e: Exception) {
            false
        }
    }
}