package com.example.milsabores

import com.example.milsabores.data.local.entity.Producto
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.ui.viewmodel.CatalogoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule // Importante
import org.junit.Test
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

// NOTA: He borrado la clase 'class MainDispatcherRule ...' de aquí porque
// ya la tienes en otro archivo. Si la dejas aquí, te dará error de duplicado.

class CatalogoViewModelTest {

    // --- CORRECCIÓN CRÍTICA ---
    // Te faltaba esta línea. Sin ella, la regla existe pero no se usa.
    // Esto conecta el TestWatcher con la ejecución del test.
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val productoRepository: ProductoRepository = mock()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `9 filtrar por categoria actualiza el estado correctamente`() = runTest {
        // GIVEN
        val listaVacia = emptyList<Producto>()

        // Simulamos respuestas vacías
        whenever(productoRepository.obtenerTodos()).thenReturn(flowOf(listaVacia))
        whenever(productoRepository.obtenerPorCategoria(anyOrNull())).thenReturn(flowOf(listaVacia))
        whenever(productoRepository.buscarProductos(anyOrNull(), anyOrNull(), anyOrNull())).thenAnswer { }

        val viewModel = CatalogoViewModel(productoRepository)

        // WHEN
        viewModel.actualizarCategoria("vegana")
        advanceUntilIdle() // Esperamos a que termine la corrutina

        // THEN
        assertEquals("vegana", viewModel.uiState.value.categoriaSeleccionada)
    }

    @Test
    fun `10 maneja error del repositorio correctamente`() = runTest {
        val listaVacia = emptyList<Producto>()
        whenever(productoRepository.obtenerTodos()).thenReturn(flowOf(listaVacia))
        whenever(productoRepository.buscarProductos(anyOrNull(), anyOrNull(), anyOrNull())).thenAnswer { }

        val viewModel = CatalogoViewModel(productoRepository)

        assertEquals("todos", viewModel.uiState.value.categoriaSeleccionada)
    }
}