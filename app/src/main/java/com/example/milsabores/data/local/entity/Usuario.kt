package com.example.milsabores.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val id: Int,
    val email: String,
    val password: String,
    val rol: String
)