package com.example.milsabores.data.repository

import com.example.milsabores.data.local.dao.CarritoDao
import com.example.milsabores.data.local.entity.ItemCarrito
import com.example.milsabores.data.local.entity.Producto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CarritoRepositoryTest {

    // Mock del DAO (Simulamos la base de datos)
    private val carritoDao: CarritoDao = mock()

    // El Repositorio a probar
    private val repository = CarritoRepositoryImpl(carritoDao)

    // Datos de prueba
    private val productoFake = Producto(1, "C1", "cat", "Torta", 5000, "desc", "img", "icon")

    @Test
    fun obtenerItems_devuelve_flujo_del_DAO() = runTest {
        // GIVEN
        val listaFake = listOf(ItemCarrito(1, 2))
        whenever(carritoDao.obtenerItems()).thenReturn(flowOf(listaFake))

        // WHEN
        val resultado = repository.obtenerItems().first()

        // THEN
        assertEquals(1, resultado.size)
        assertEquals(2, resultado[0].cantidad)
    }

    @Test
    fun agregarAlCarrito_Inserta_NUEVO_Item_SiNoEexiste() = runTest {
        // GIVEN: El DAO dice que no existe el item (devuelve null)
        whenever(carritoDao.obtenerItemPorId(1)).thenReturn(null)

        // WHEN: Agregamos 2 unidades
        repository.agregarAlCarrito(productoFake, 2)

        // THEN: Verificamos que se insertó un item con cantidad 2
        val captor = argumentCaptor<ItemCarrito>()
        verify(carritoDao).insertar(captor.capture())

        assertEquals(1, captor.firstValue.productoId)
        assertEquals(2, captor.firstValue.cantidad)
    }

    @Test
    fun agregarAlCarrito_SUMA_Cantidad_SiYaExiste() = runTest {
        // GIVEN: El DAO dice que YA existe con cantidad 3
        val itemExistente = ItemCarrito(1, 3)
        whenever(carritoDao.obtenerItemPorId(1)).thenReturn(itemExistente)

        // WHEN: Agregamos 2 unidades más
        repository.agregarAlCarrito(productoFake, 2)

        // THEN: Verificamos que se insertó (actualizó) con cantidad 5 (3 + 2)
        val captor = argumentCaptor<ItemCarrito>()
        verify(carritoDao).insertar(captor.capture())

        assertEquals(5, captor.firstValue.cantidad)
    }

    @Test
    fun actualizarCantidad_Sobrescribe_Valor() = runTest {
        // GIVEN: Existe el item
        whenever(carritoDao.obtenerItemPorId(1)).thenReturn(ItemCarrito(1, 5))

        // WHEN: Cambiamos la cantidad a 10 (fijo)
        repository.actualizarCantidad(1, 10)

        // THEN: Se guarda con 10
        val captor = argumentCaptor<ItemCarrito>()
        verify(carritoDao).insertar(captor.capture())

        assertEquals(10, captor.firstValue.cantidad)
    }
}