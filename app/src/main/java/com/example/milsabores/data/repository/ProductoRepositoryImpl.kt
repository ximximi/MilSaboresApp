package com.example.milsabores.data.repository

import android.util.Log
import com.example.milsabores.data.local.dao.ProductoDao
import com.example.milsabores.data.local.entity.Producto
import com.example.milsabores.data.remote.api.MilSaboresApi
import com.example.milsabores.data.remote.toEntityList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class ProductoRepositoryImpl(
    private val dao: ProductoDao,
    private val api: MilSaboresApi // <-- ¡NUEVO! Pedimos la API
) : ProductoRepository {

    override fun obtenerTodos(): Flow<List<Producto>> {
        // 1. Devolvemos lo que hay en la base de datos (Room)
        return dao.obtenerTodos().onStart {
            // 2. AL MISMO TIEMPO (en paralelo), intentamos actualizar desde Internet
            refreshProductos()
        }
    }

    override fun obtenerPorCategoria(categoria: String): Flow<List<Producto>> {
        return dao.obtenerPorCategoria(categoria)
            // También refrescamos al filtrar, por si acaso
            .onStart { refreshProductos() }
    }

    override fun obtenerPorId(id: Int): Flow<Producto> {
        return dao.obtenerPorId(id)
    }

    // --- Lógica para sincronizar con la API ---
    private suspend fun refreshProductos() {
        try {
            // A. Llamada a la API (Retrofit)
            val productosDto = api.obtenerProductos()

            // B. Mapeo (Dto -> Entity)
            val productosEntity = productosDto.toEntityList()

            // C. Guardar en BD (Room)
            // Esto disparará automáticamente el Flow y actualizará la pantalla
            if (productosEntity.isNotEmpty()) {
                dao.insertarTodos(productosEntity)
                Log.d("API_SUCCESS", "Se cargaron ${productosEntity.size} productos de la API")
            }

        } catch (e: Exception) {
            // Si falla (no hay internet), no hacemos nada.
            // La app sigue mostrando los datos viejos de la BD.
            Log.e("API_ERROR", "Error al conectar con la API: ${e.message}")
        }
    }
}