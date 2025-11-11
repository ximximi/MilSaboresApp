package com.example.milsabores.di

import com.example.milsabores.data.repository.ProductoRepository

/**
 * Contrato para nuestro contenedor de dependencias.
 * Define QUÉ repositorios puede proveer.
 */
interface AppContainer {
    val productoRepository: ProductoRepository
    // Aquí añadiremos el CarritoRepository, UsuarioRepository, etc. más adelante
}