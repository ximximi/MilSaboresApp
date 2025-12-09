package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.local.entity.Producto
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

    // Variable para guardar la lista "cruda" que viene de la BD
    private var listaCache: List<Producto> = emptyList()

    init {
        cargarDatos()
    }

    // --- ACCIONES DEL USUARIO ---

    fun actualizarCategoria(nuevaCategoria: String) {
        _uiState.update { it.copy(categoriaSeleccionada = nuevaCategoria) }
        // Al cambiar categoría, reiniciamos el flujo de datos completo
        cargarDatos()
    }

    fun actualizarBusqueda(query: String) {
        _uiState.update { it.copy(busqueda = query) }
        // 1. Re-filtramos INMEDIATAMENTE lo que ya tenemos en pantalla (Reactividad pura)
        actualizarUiLocalmente()
        // 2. Pedimos a la API en segundo plano
        aplicarFiltrosApi()
    }

    fun actualizarOrden(ascendente: Boolean?) {
        _uiState.update { it.copy(ordenAscendente = ascendente) }
        // 1. Re-ordenamos INMEDIATAMENTE
        actualizarUiLocalmente()
        // 2. Pedimos a la API en segundo plano
        aplicarFiltrosApi()
    }

    // --- LÓGICA INTERNA ---

    private fun cargarDatos() {
        val categoria = _uiState.value.categoriaSeleccionada

        viewModelScope.launch {
            val flow = if (categoria == "todos") {
                productoRepository.obtenerTodos()
            } else {
                productoRepository.obtenerPorCategoria(categoria)
            }

            flow
                .onStart { _uiState.update { it.copy(estaCargando = true) } }
                .catch { e -> _uiState.update { it.copy(error = e.message) } }
                .collect { productosDb ->
                    // 1. Guardamos la lista original en nuestra caché
                    listaCache = productosDb

                    // 2. Aplicamos los filtros y actualizamos la UI
                    actualizarUiLocalmente()
                }
        }
        // Disparar carga inicial de API
        aplicarFiltrosApi()
    }

    private fun actualizarUiLocalmente() {
        // Tomamos la lista caché y le aplicamos los filtros actuales (texto y orden)
        val productosFiltrados = filtrarListaEnMemoria(listaCache)

        _uiState.update {
            it.copy(
                estaCargando = false,
                productos = productosFiltrados
            )
        }
    }

    private fun aplicarFiltrosApi() {
        viewModelScope.launch {
            val state = _uiState.value
            val catParaApi = if (state.categoriaSeleccionada == "todos") null else state.categoriaSeleccionada

            // Llamada segura a la API
            productoRepository.buscarProductos(
                categoria = catParaApi,
                nombre = state.busqueda.ifEmpty { null },
                precioAscendente = state.ordenAscendente
            )
        }
    }

    private fun filtrarListaEnMemoria(lista: List<Producto>): List<Producto> {
        val state = _uiState.value
        var resultado = lista

        // 1. Filtro de Texto
        if (state.busqueda.isNotEmpty()) {
            resultado = resultado.filter {
                it.nombre.contains(state.busqueda, ignoreCase = true)
            }
        }

        // 2. Ordenamiento
        resultado = when (state.ordenAscendente) {
            true -> resultado.sortedBy { it.precio }
            false -> resultado.sortedByDescending { it.precio }
            null -> resultado
        }

        return resultado
    }
}