package com.example.milsabores.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blog")
data class Blog(
    @PrimaryKey val id: Int,
    val titulo: String,
    val fecha: String,
    val resumen: String,
    val imagen: String
)