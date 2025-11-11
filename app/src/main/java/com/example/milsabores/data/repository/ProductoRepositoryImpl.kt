package com.example.milsabores.data.repository

import com.example.milsabores.data.local.dao.ProductoDao
import com.example.milsabores.data.local.entity.Producto
import kotlinx.coroutines.flow.Flow

/**
 * Esta es la implementación (el "trabajador") del contrato.
 * Sabe CÓMO obtener los datos.
 */
class ProductoRepositoryImpl(
    private val dao: ProductoDao // Pide el DAO para poder hablar con Room
) : ProductoRepository { // <-- ": ProductoRepository" significa que CUMPLE el contrato

    // Implementación del CÓMO
    override fun obtenerTodos(): Flow<List<Producto>> {
        // El "cómo" es simple: solo llama al DAO
        return dao.obtenerTodos()
    }

    override fun obtenerPorId(id: Int): Flow<Producto> {
        return dao.obtenerPorId(id)
    }
}