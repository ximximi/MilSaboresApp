package com.example.milsabores.data.repository

import com.example.milsabores.data.local.entity.ItemCarrito
import com.example.milsabores.data.local.entity.Producto
import kotlinx.coroutines.flow.Flow

/**
 * Contrato para el Repositorio del Carrito.
 */
interface CarritoRepository {

    fun obtenerItems(): Flow<List<ItemCarrito>>

    suspend fun agregarAlCarrito(producto: Producto, cantidad: Int)

    suspend fun eliminarDelCarrito(productoId: Int)

    suspend fun limpiarCarrito()

    suspend fun actualizarCantidad(productoId: Int, nuevaCantidad: Int)}