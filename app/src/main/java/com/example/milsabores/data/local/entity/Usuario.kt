package com.example.milsabores.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * ¡Esta es la entidad Usuario MODIFICADA!
 *
 * CAMBIOS:
 * 1. @PrimaryKey: Ahora tiene (autoGenerate = true) y un valor por defecto
 * (id: Int = 0) para que Room genere el ID por nosotros en el registro.
 * 2. @Entity: Añadimos 'indices' para hacer que el email sea 'unique'.
 * Esto asegura que la BD falle si intentamos registrar un email duplicado

 * 3. Campos: Eliminamos 'rol' y añadimos 'nombre' y 'direccion' (nulable)
 * para que coincida con nuestro formulario de registro y el PasteleriaDatabase.kt.
 */
@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["email"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String,
    val nombre: String,
    val direccion: String?,
    val fotoPerfil: String? = null
)