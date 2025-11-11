package com.example.milsabores.data.repository

import com.example.milsabores.data.local.entity.Producto
import kotlinx.coroutines.flow.Flow

/**
 * Esta es la interfaz (el "contrato") para el repositorio de productos.
 * Le dice al ViewModel QUÉ puede pedir.
 */
interface ProductoRepository {

    // Contrato: "Cualquiera que me use, puede pedirme todos los productos"
    fun obtenerTodos(): Flow<List<Producto>>

    // Contrato: "Cualquiera que me use, puede pedirme un producto por su ID"
    fun obtenerPorId(id: Int): Flow<Producto>
}