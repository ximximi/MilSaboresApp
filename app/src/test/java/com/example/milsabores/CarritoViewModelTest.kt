package com.example.milsabores

import com.example.milsabores.data.local.entity.ItemCarrito
import com.example.milsabores.data.local.entity.Producto
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.ui.viewmodel.CarritoViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CarritoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val carritoRepository: CarritoRepository = mock()
    private val productoRepository: ProductoRepository = mock()

    // Datos Fake
    private val producto1 = Producto(1, "C1", "cat", "Torta", 20000, "desc", "img", "icon") // 20.000
    private val producto2 = Producto(2, "C2", "cat", "Pie", 10000, "desc", "img", "icon")   // 10.000

    @Test
    fun `1 calcula subtotal y total correctamente con productos`() = runTest {
        // GIVEN: 1 Torta (20k) + 2 Pies (20k) = 40k Subtotal
        val items = listOf(ItemCarrito(1, 1), ItemCarrito(2, 2))

        whenever(productoRepository.obtenerTodos()).thenReturn(flowOf(listOf(producto1, producto2)))
        whenever(carritoRepository.obtenerItems()).thenReturn(flowOf(items))

        // WHEN
        val viewModel = CarritoViewModel(carritoRepository, productoRepository)

        // THEN
        // Subtotal: 40.000
        // Envío: 3.000 (porque es menor a 50k)
        // Total: 43.000
        assertEquals(40000.0, viewModel.uiState.value.subtotal, 0.0)
        assertEquals(43000.0, viewModel.uiState.value.total, 0.0)
    }

    @Test
    fun `2 aplica envio gratis si supera 50000`() = runTest {
        // GIVEN: 3 Tortas (60k) -> Supera 50k
        val items = listOf(ItemCarrito(1, 3))

        whenever(productoRepository.obtenerTodos()).thenReturn(flowOf(listOf(producto1)))
        whenever(carritoRepository.obtenerItems()).thenReturn(flowOf(items))

        // WHEN
        val viewModel = CarritoViewModel(carritoRepository, productoRepository)

        // THEN
        assertEquals(0.0, viewModel.uiState.value.costoEnvio, 0.0) // Envío Gratis
        assertEquals(60000.0, viewModel.uiState.value.total, 0.0)
    }

    @Test
    fun `3 cobra envio si no supera 50000`() = runTest {
        // GIVEN: 1 Torta (20k)
        val items = listOf(ItemCarrito(1, 1))

        whenever(productoRepository.obtenerTodos()).thenReturn(flowOf(listOf(producto1)))
        whenever(carritoRepository.obtenerItems()).thenReturn(flowOf(items))

        // WHEN
        val viewModel = CarritoViewModel(carritoRepository, productoRepository)

        // THEN
        assertEquals(3000.0, viewModel.uiState.value.costoEnvio, 0.0) // Cobra envío
    }

    @Test
    fun `4 aplica codigo de descuento valido (DULCE10)`() = runTest {
        // GIVEN: 1 Torta (20k)
        val items = listOf(ItemCarrito(1, 1))
        whenever(productoRepository.obtenerTodos()).thenReturn(flowOf(listOf(producto1)))
        whenever(carritoRepository.obtenerItems()).thenReturn(flowOf(items))
        val viewModel = CarritoViewModel(carritoRepository, productoRepository)

        // WHEN: Aplicamos cupón del 10%
        viewModel.aplicarCodigo("DULCE10")

        // THEN
        // Descuento esperado: 10% de 20.000 = 2.000
        assertEquals(2000.0, viewModel.uiState.value.montoDescuento, 0.0)
        // Total: 20.000 + 3.000 (envío) - 2.000 (descuento) = 21.000
        assertEquals(21000.0, viewModel.uiState.value.total, 0.0)
    }

    @Test
    fun `5 ignora codigo de descuento invalido`() = runTest {
        val items = listOf(ItemCarrito(1, 1))
        whenever(productoRepository.obtenerTodos()).thenReturn(flowOf(listOf(producto1)))
        whenever(carritoRepository.obtenerItems()).thenReturn(flowOf(items))
        val viewModel = CarritoViewModel(carritoRepository, productoRepository)

        // WHEN
        viewModel.aplicarCodigo("CODIGO_FALSO")

        // THEN
        assertEquals(0.0, viewModel.uiState.value.montoDescuento, 0.0)
    }
}