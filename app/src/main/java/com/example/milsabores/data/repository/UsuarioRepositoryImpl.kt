package com.example.milsabores.data.repository

import com.example.milsabores.data.local.dao.UsuarioDao
import com.example.milsabores.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * Esta es la implementación (el "trabajador") del contrato de Usuario.
 * Sabe CÓMO obtener los datos.
 * (Sigue el patrón de ProductoRepositoryImpl)
 */
class UsuarioRepositoryImpl(
    private val dao: UsuarioDao // <-- ¡Sigue tu patrón!
) : UsuarioRepository { // <-- ": UsuarioRepository" significa que CUMPLE el contrato

    override suspend fun login(email: String, password: String): Usuario? {
        // El "cómo" es simple: solo llama al DAO
        return dao.validarCredenciales(email, password)
    }

    override suspend fun registrarUsuario(usuario: Usuario): Result<Unit> {
        // El "cómo" del registro (con la lógica de validación que ya teníamos)
        return try {
            val usuarioExistente = dao.obtenerUsuarioPorEmail(usuario.email)
            if (usuarioExistente != null) {
                Result.failure(Exception("El correo electrónico ya está registrado."))
            } else {
                dao.registrarUsuario(usuario)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUsuario(usuarioId: Int): Flow<Usuario?> {
        return dao.obtenerUsuarioPorId(usuarioId)
    }
}
