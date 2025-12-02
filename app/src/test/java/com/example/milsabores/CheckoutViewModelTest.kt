package com.example.milsabores

import com.example.milsabores.data.local.DatosCompra
import com.example.milsabores.data.local.entity.ItemCarrito
import com.example.milsabores.data.local.entity.Producto
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.di.AppContainer
import com.example.milsabores.ui.viewmodel.CheckoutViewModel
import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify

class CheckoutViewModelTest {

    // 1. Regla para Corrutinas (necesaria para ViewModelScope)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // 2. MOCKS: Creamos objetos falsos automáticamente con Mockito
    // "mock()" crea un objeto vacío que finge ser la clase real.
    private val carritoRepository: CarritoRepository = mock()
    private val productoRepository: ProductoRepository = mock()
    private val appContainer: AppContainer = mock()
    private val productoPrueba = Producto(1, "C01", "cat", "Torta", 5000, "desc", "img", "icon")
    private val itemCarritoPrueba = ItemCarrito(1, 2) // 2 Tortas

    @Test
    fun `al iniciar, calcula el total correctamente`() = runTest {
        // GIVEN (Dado que):
        // Configuramos el comportamiento de los Mocks.
        // "Cuando te pidan items, devuelve este flujo..."
        whenever(carritoRepository.obtenerItems()).thenReturn(flowOf(listOf(itemCarritoPrueba)))
        // "Cuando te pidan productos, devuelve esta lista..."
        whenever(productoRepository.obtenerTodos()).thenReturn(flowOf(listOf(productoPrueba)))

        // Inicializamos el ViewModel con los Mocks
        val viewModel = CheckoutViewModel(carritoRepository, productoRepository, appContainer)

        // WHEN (Cuando) & THEN (Entonces):
        // Usamos TURBINE para observar el flujo de estados (uiState)
        // WHEN (Cuando) & THEN (Entonces):
        viewModel.uiState.test {
            // Como usamos UnconfinedTestDispatcher, el cálculo ocurre inmediatamente en el init.
            // El primer item que recibimos YA ES el estado calculado.
            val estadoCalculado = awaitItem()

            // Verificamos el cálculo: (5000 * 2) + 3000 envío = 13000
            // (Precio 5000 x 2 unidades + 3000 envío)
            assertEquals(13000.0, estadoCalculado.totalAPagar, 0.0)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `validar formulario detecta campos vacios`() = runTest {
        // GIVEN: Repositorios vacíos para este test
        whenever(carritoRepository.obtenerItems()).thenReturn(flowOf(emptyList()))
        whenever(productoRepository.obtenerTodos()).thenReturn(flowOf(emptyList()))

        val viewModel = CheckoutViewModel(carritoRepository, productoRepository, appContainer)

        // WHEN: Intentamos pagar sin llenar nada
        viewModel.onPagarClick()

        // THEN: Verificamos que el estado tenga errores
        val estado = viewModel.uiState.value

        assertFalse(estado.pagoExitoso) // No debe haber pagado
        assertNotNull(estado.errorNombre) // Debe haber error en el nombre
        assertEquals("Nombre no puede estar vacío", estado.errorNombre)
    }

    @Test
    fun `flujo de pago exitoso limpia carrito y navega`() = runTest {
        // GIVEN:
        whenever(carritoRepository.obtenerItems()).thenReturn(flowOf(emptyList()))
        whenever(productoRepository.obtenerTodos()).thenReturn(flowOf(emptyList()))

        val viewModel = CheckoutViewModel(carritoRepository, productoRepository, appContainer)

        // Llenamos el formulario correctamente
        viewModel.onFieldChange("nombre", "Juan Perez")
        viewModel.onFieldChange("email", "juan@gmail.com")
        viewModel.onFieldChange("telefono", "12345678")
        viewModel.onFieldChange("calle", "Calle Falsa")
        viewModel.onFieldChange("numeroCalle", "123")
        viewModel.onFieldChange("comuna", "Santiago")
        viewModel.onFieldChange("fechaEntrega", "01/01/2025")
        viewModel.onFieldChange("metodoPago", "webpay")

        // WHEN: Pagamos
        viewModel.onPagarClick()

        // THEN:
        // 1. Verificamos que el estado cambia a procesando y luego a exitoso
        // (Nota: En un test real con Turbine, veríamos la secuencia de estados.
        // Aquí verificamos el resultado final y las llamadas).

        // Verificamos que se llamó a limpiarCarrito en el repositorio
        // (Mockito verifica que la función fue llamada)
        verify(carritoRepository).limpiarCarrito()

        assertTrue(viewModel.uiState.value.pagoExitoso)
    }
    @Test
    fun `validar formulario detecta email invalido`() = runTest {
        whenever(carritoRepository.obtenerItems()).thenReturn(flowOf(emptyList()))
        whenever(productoRepository.obtenerTodos()).thenReturn(flowOf(emptyList()))
        val viewModel = CheckoutViewModel(carritoRepository, productoRepository, appContainer)

        // WHEN: Ponemos un email sin arroba
        viewModel.onFieldChange("email", "correo_falso.com")
        viewModel.onPagarClick()

        // THEN
        assertFalse(viewModel.uiState.value.pagoExitoso)
        assertEquals("Email inválido", viewModel.uiState.value.errorEmail)
    }
}