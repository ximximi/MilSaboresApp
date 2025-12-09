package com.example.milsabores.data.repository

import com.example.milsabores.data.local.dao.ProductoDao
import com.example.milsabores.data.local.entity.Producto
import com.example.milsabores.data.remote.api.MilSaboresApi
import com.example.milsabores.data.remote.dto.ProductoDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ProductoRepositoryTest {

    // Mocks: Simulamos las dependencias externas
    private val productoDao: ProductoDao = mock()
    private val milSaboresApi: MilSaboresApi = mock()

    // El Repositorio que vamos a probar
    private val repository = ProductoRepositoryImpl(productoDao, milSaboresApi)

    @Test
    fun obtenerItems_debeDevolverFlujo_desdeDAO() = runTest {
        // GIVEN (Dado que):
        // 1. El DAO tiene datos locales
        val datosLocales = listOf(Producto(1, "C1", "cat", "Torta Local", 5000, "desc", "img", "icon"))
        whenever(productoDao.obtenerTodos()).thenReturn(flowOf(datosLocales))

        // 2. La API devuelve datos remotos (DTOs)
        val datosRemotos = listOf(ProductoDto("1", "Torta API", 6000.0, "desc", "img", "cat"))
        whenever(milSaboresApi.obtenerProductos()).thenReturn(datosRemotos)

        // WHEN (Cuando): Llamamos al repositorio
        val flujo = repository.obtenerTodos()

        // THEN (Entonces):

        // 1. Verificamos que recibimos los datos locales primero (el "Single Source of Truth")
        val resultado = flujo.first()
        assertEquals("Torta Local", resultado[0].nombre)

        // 2. Verificamos que el repositorio LLAMÓ a la API (en el fondo) para actualizar
        // (Esto confirma que la lógica de "Refresh" se ejecutó)
        verify(milSaboresApi).obtenerProductos()

        // 3. Verificamos que intentó guardar los nuevos datos en el DAO
        verify(productoDao).insertarTodos(any())
    }
}