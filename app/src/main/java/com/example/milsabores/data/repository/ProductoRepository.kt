package com.example.milsabores.data.repository

import com.example.milsabores.data.local.entity.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun obtenerTodos(): Flow<List<Producto>>
    fun obtenerPorId(id: Int): Flow<Producto>
    fun obtenerPorCategoria(categoria: String): Flow<List<Producto>>

    // ESTA es la nueva función que agregamos al contrato
    suspend fun buscarProductos(categoria: String?, nombre: String?, precioAscendente: Boolean?)
}