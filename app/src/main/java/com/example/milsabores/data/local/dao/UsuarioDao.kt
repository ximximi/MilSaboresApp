package com.example.milsabores.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.milsabores.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow // <-- ¡Añadimos el import para Flow!

@Dao
interface UsuarioDao {

    /**
     * Inserta la lista inicial de usuarios desde el JSON.
     * (Cambié 'insertarUsuarios' por 'insertarTodos' para que
     * sea consistente con tu ProductoDao y con PasteleriaDatabase.kt)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(usuarios: List<Usuario>)

    /**
     * Busca un usuario por email y contraseña (para el login)
     * ¡Esta es la función que YA TENÍAS!
     */
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun validarCredenciales(email: String, password: String): Usuario?

    // --- ¡¡FUNCIONES NUEVAS QUE NECESITAMOS!! ---

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
}
