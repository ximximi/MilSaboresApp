package com.example.milsabores.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.milsabores.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(usuarios: List<Usuario>)
    /**
     * Busca un usuario por email y contraseña (para el login)
     */
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun validarCredenciales(email: String, password: String): Usuario?
    /**
     * Busca un usuario solo por email (para chequear si ya existe).
     * Lo necesita 'UsuarioRepositoryImpl' para la lógica de registro.
     */
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun obtenerUsuarioPorEmail(email: String): Usuario?
    /**
     * Inserta un *nuevo* usuario (para la pantalla de Registro).
     * OnConflictStrategy.ABORT falla si el email ya existe (¡perfecto!).
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrarUsuario(usuario: Usuario)
    /**
     * Obtiene un usuario por su ID (para un futuro perfil).
     * Devuelve un Flow para que se actualice si el perfil cambia.
     */
    @Query("SELECT * FROM usuarios WHERE id = :usuarioId")
    fun obtenerUsuarioPorId(usuarioId: Int): Flow<Usuario?>
    @Query("UPDATE usuarios SET fotoPerfil = :fotoUri WHERE id = :usuarioId")
    suspend fun actualizarFotoPerfil(usuarioId: Int, fotoUri: String)
}
