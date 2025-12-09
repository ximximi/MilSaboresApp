package com.example.milsabores

import androidx.lifecycle.SavedStateHandle
import com.example.milsabores.data.local.entity.Producto
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.ui.viewmodel.DetalleViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DetalleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val productoRepository: ProductoRepository = mock()
    private val carritoRepository: CarritoRepository = mock()

    @Test
    fun `al iniciar, carga el producto correcto usando el ID`() = runTest {
        // GIVEN: Simulamos que navegamos al producto con ID 99
        val savedStateHandle = SavedStateHandle(mapOf("productoId" to 99))
        val productoFake = Producto(99, "C99", "cat", "Torta Test", 5000, "desc", "img", "icon")

        whenever(productoRepository.obtenerPorId(99)).thenReturn(flowOf(productoFake))

        // WHEN
        val viewModel = DetalleViewModel(savedStateHandle, productoRepository, carritoRepository)

        // THEN
        val estado = viewModel.uiState.value
        assertFalse(estado.estaCargando)
        assertEquals("Torta Test", estado.producto?.nombre)
    }

    @Test
    fun `agregar al carrito llama al repositorio con cantidad 1`() = runTest {
        // GIVEN
        val savedStateHandle = SavedStateHandle(mapOf("productoId" to 99))
        val productoFake = Producto(99, "C99", "cat", "Torta Test", 5000, "desc", "img", "icon")
        whenever(productoRepository.obtenerPorId(99)).thenReturn(flowOf(productoFake))

        val viewModel = DetalleViewModel(savedStateHandle, productoRepository, carritoRepository)

        // Esperamos a que cargue el producto (en el entorno de test es inmediato)

        // WHEN: Clic en agregar
        viewModel.agregarAlCarrito()

        // THEN: Verificamos que llamó al repositorio para agregar 1 unidad
        verify(carritoRepository).agregarAlCarrito(eq(productoFake), eq(1))
    }
}