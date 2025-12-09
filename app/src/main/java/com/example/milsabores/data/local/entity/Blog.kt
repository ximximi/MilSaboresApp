package com.example.milsabores.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blog")
data class Blog(
    @PrimaryKey val id: Int,
    val categoria: String,
    val titulo: String,
    val resumen: String,
    val fecha: String,
    val autor: String,
    val imagen: String,
    val contenido: String
)