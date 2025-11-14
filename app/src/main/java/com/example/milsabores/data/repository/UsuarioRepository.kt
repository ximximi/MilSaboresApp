package com.example.milsabores.data.repository

import com.example.milsabores.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * Esta es la interfaz (el "contrato") para el repositorio de usuarios.
 * Le dice al ViewModel QUÉ puede pedir.
 * Sigue el patrón de ProductoRepository.
 */
interface UsuarioRepository {

    // Contrato: "Cualquiera que me use, puede pedirme hacer login"
    suspend fun login(email: String, password: String): Usuario?

    // Contrato: "Cualquiera que me use, puede pedirme registrar un usuario"
    suspend fun registrarUsuario(usuario: Usuario): Result<Unit>

    // Contrato: "Cualquiera que me use, puede pedirme un usuario por su ID"
    fun getUsuario(usuarioId: Int): Flow<Usuario?>
}

