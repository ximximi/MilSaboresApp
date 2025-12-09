package com.example.milsabores

import com.example.milsabores.data.local.entity.Blog
import com.example.milsabores.data.local.entity.Producto
import com.example.milsabores.data.repository.BlogRepository
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val productoRepository: ProductoRepository = mock()
    private val blogRepository: BlogRepository = mock()

    @Test
    fun `al iniciar, carga productos y blogs combinados correctamente`() = runTest {
        // GIVEN: Datos simulados
        val listaProductos = listOf(Producto(1, "C1", "cat", "Torta", 5000, "desc", "img", "icon"))
        val listaBlogs = listOf(Blog(1, "cat", "Titulo", "Resumen", "Fecha", "Autor", "img", "html"))

        whenever(productoRepository.obtenerTodos()).thenReturn(flowOf(listaProductos))
        whenever(blogRepository.obtenerTodos()).thenReturn(flowOf(listaBlogs))

        // WHEN: Iniciamos el ViewModel
        val viewModel = HomeViewModel(productoRepository, blogRepository)

        // THEN: Verificamos que el estado tenga ambas listas
        val estado = viewModel.uiState.value

        assertFalse(estado.estaCargando)
        assertEquals(1, estado.productos.size)
        assertEquals("Torta", estado.productos[0].nombre)
        assertEquals(1, estado.entradasBlog.size)
        assertEquals("Titulo", estado.entradasBlog[0].titulo)
    }
}