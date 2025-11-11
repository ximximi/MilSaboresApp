package com.example.milsabores.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito")
data class ItemCarrito(
    // Usaremos el ID del producto como ID principal.
    // Así, no puedes añadir el mismo producto dos veces (solo actualizar la cantidad).
    @PrimaryKey val productoId: Int,
    val cantidad: Int
)