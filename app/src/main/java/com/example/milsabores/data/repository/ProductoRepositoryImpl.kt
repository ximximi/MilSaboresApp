package com.example.milsabores.data.repository

import android.util.Log
import com.example.milsabores.data.local.dao.ProductoDao
import com.example.milsabores.data.local.entity.Producto
import com.example.milsabores.data.remote.api.MilSaboresApi
import com.example.milsabores.data.remote.dto.ProductoDto
// Asegúrate de tener tu mapper importado o la función auxiliar abajo
import com.example.milsabores.data.remote.toEntityList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class ProductoRepositoryImpl(
    private val dao: ProductoDao,
    private val api: MilSaboresApi
) : ProductoRepository {

    // Mantenemos obtenerTodos simple (sin filtros) para la carga inicial
    override fun obtenerTodos(): Flow<List<Producto>> {
        return dao.obtenerTodos().onStart {
            refreshProductos(categoria = null, nombre = null, orden = null)
        }
    }

    override fun obtenerPorCategoria(categoria: String): Flow<List<Producto>> {
        return dao.obtenerPorCategoria(categoria).onStart {
            // Pedimos a la API solo esa categoría
            refreshProductos(categoria = categoria, nombre = null, orden = null)
        }
    }

    override fun obtenerPorId(id: Int): Flow<Producto> {
        return dao.obtenerPorId(id).onStart {
            refreshPorId(id)
        }
    }

    // --- NUEVA FUNCIÓN: Búsqueda con Filtros ---
    // Esta función la llamarás desde tu ViewModel cuando el usuario aplique filtros
    override suspend fun buscarProductos(categoria: String?, nombre: String?, precioAscendente: Boolean?) {
        val orden = if (precioAscendente == true) "asc" else if (precioAscendente == false) "desc" else null
        val ordenarPor = if (orden != null) "precio" else null

        refreshProductos(categoria, nombre, ordenarPor, orden)
    }


    // --- PRIVADAS: Conexión con MockAPI ---

    private suspend fun refreshProductos(
        categoria: String?,
        nombre: String?,
        ordenarPor: String? = null,
        orden: String? = null
    ) {
        try {
            // Llamamos a la "Súper Función" de la API con los filtros
            val dtos = api.obtenerProductos(
                categoria = categoria,
                nombre = nombre,
                ordenarPor = ordenarPor,
                orden = orden
            )

            if (dtos.isNotEmpty()) {
                // Truco: Si estamos filtrando, MockAPI nos devuelve solo una parte del catálogo.
                // Insertamos (o actualizamos) solo esos productos en Room.
                dao.insertarTodos(dtos.toEntityList())
                Log.d("API_REPO", "Productos cargados: ${dtos.size}")
            }
        } catch (e: Exception) {
            Log.e("API_REPO", "Error cargando productos: ${e.message}")
        }
    }

    private suspend fun refreshPorId(id: Int) {
        try {
            val dto = api.obtenerProductoPorId(id.toString())
            // Aquí necesitarás tu mapper de un solo objeto.
            // Si no tienes 'toEntity()', usa la función manual que te pasé antes.
            // dao.insertarProducto(dto.toEntity())
        } catch (e: Exception) {
            Log.e("API_REPO", "Error refreshPorId: ${e.message}")
        }
    }
}