package com.example.milsabores.di

import com.example.milsabores.data.repository.BlogRepository
import com.example.milsabores.data.repository.ProductoRepository

/**
 * Contrato para nuestro contenedor de dependencias.
 * Define QUÉ repositorios puede proveer.
 */
interface AppContainer {
    val productoRepository: ProductoRepository
    val blogRepository: BlogRepository
    // Aquí añadiremos el CarritoRepository, UsuarioRepository, etc. más adelante
}