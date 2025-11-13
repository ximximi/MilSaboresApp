package com.example.milsabores.di

import android.content.Context
import com.example.milsabores.data.local.PasteleriaDatabase
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.data.repository.ProductoRepositoryImpl
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.data.repository.CarritoRepositoryImpl


/**
 * Implementación del contenedor. Sabe CÓMO construir los repositorios.
 */
class AppDataContainer(
    private val context: Context // Necesita el contexto para crear la BD
) : AppContainer {

    // Construye la base de datos (usando el singleton que creamos)
    private val database: PasteleriaDatabase by lazy {
        PasteleriaDatabase.obtenerInstancia(context)
    }

    // Construye el repositorio de productos,
    // dándole el DAO que saca de la base de datos.
    override val productoRepository: ProductoRepository by lazy {
        ProductoRepositoryImpl(database.productoDao())
    }

    override val carritoRepository: CarritoRepository by lazy {
        CarritoRepositoryImpl(database.carritoDao())
    }
}