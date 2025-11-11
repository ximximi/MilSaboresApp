package com.example.milsabores.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity le dice a Room que esta clase es una tabla.
// "tableName" es el nombre que tendrá la tabla en la base de datos.
@Entity(tableName = "productos")
data class Producto(
    // @PrimaryKey le dice a Room cuál es la columna de ID.
    @PrimaryKey val id: Int,
    val codigo: String,
    val categoria: String,
    val nombre: String,
    val precio: Int, // Usamos Int porque tu JSON tiene números enteros
    val descripcion: String,
    val imagen: String,
    val icono: String
)