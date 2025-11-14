package com.example.milsabores.di

import com.example.milsabores.data.local.DatosCompra
import com.example.milsabores.data.repository.BlogRepository
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.data.repository.UsuarioRepository

interface AppContainer {
    val productoRepository: ProductoRepository
    val carritoRepository: CarritoRepository // <-- De la rama 'catalogo'
    val blogRepository: BlogRepository       // <-- De la rama 'blog'
    var ultimaCompra: DatosCompra?

    // "Prometemos" que también habrá un repositorio de usuarios.
    val usuarioRepository: UsuarioRepository
}