package com.example.milsabores.data.remote

import com.example.milsabores.data.local.entity.Producto
import com.example.milsabores.data.remote.dto.ProductoDto

// Función de extensión para convertir DTO a Entity
fun ProductoDto.toEntity(): Producto {
    return Producto(
        // Convertimos el ID de String (API) a Int (Room)
        // Si falla, usamos 0 (aunque no debería fallar si MockAPI manda números)
        id = this.id.toIntOrNull() ?: 0,

        nombre = this.nombre,
        precio = this.precio.toInt(),
        descripcion = this.descripcion,
        categoria = this.categoria,

        // Lógica inteligente para la imagen:
        // Si la API manda una URL de internet (http), la usamos.
        // Si no, asumimos que es una ruta local de assets.
        imagen = this.imagen,

        // Valores por defecto si la API no los manda
        codigo = this.codigo ?: "EXT-${this.id}",
        icono = this.icono ?: "fa-box"
    )
}

// Función para convertir una LISTA completa
fun List<ProductoDto>.toEntityList(): List<Producto> {
    return this.map { it.toEntity() }
}