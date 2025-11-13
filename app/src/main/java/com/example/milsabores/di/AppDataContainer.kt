package com.example.milsabores.di

import android.content.Context
import com.example.milsabores.data.local.DatosCompra
import com.example.milsabores.data.local.PasteleriaDatabase
import com.example.milsabores.data.repository.BlogRepository
import com.example.milsabores.data.repository.BlogRepositoryImpl
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.data.repository.CarritoRepositoryImpl
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.data.repository.ProductoRepositoryImpl

class AppDataContainer(
    private val context: Context
) : AppContainer {

    private val database: PasteleriaDatabase by lazy {
        PasteleriaDatabase.obtenerInstancia(context)
    }

    override val productoRepository: ProductoRepository by lazy {
        ProductoRepositoryImpl(database.productoDao())
    }

    // --- De la rama 'catalogo' ---
    override val carritoRepository: CarritoRepository by lazy {
        CarritoRepositoryImpl(database.carritoDao())
    }

    // --- De la rama 'blog' ---
    override val blogRepository: BlogRepository by lazy {
        BlogRepositoryImpl(database.blogDao())
    }

    override var ultimaCompra: DatosCompra? = null
}