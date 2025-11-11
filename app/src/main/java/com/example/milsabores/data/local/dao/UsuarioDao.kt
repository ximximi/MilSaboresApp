package com.example.milsabores.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.milsabores.data.local.entity.Usuario

@Dao
interface UsuarioDao {

    // Inserta usuarios (ej. admin, cliente de prueba)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuarios(usuarios: List<Usuario>)

    // Busca un usuario por email y contraseña (para el login)
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun validarCredenciales(email: String, password: String): Usuario?
}