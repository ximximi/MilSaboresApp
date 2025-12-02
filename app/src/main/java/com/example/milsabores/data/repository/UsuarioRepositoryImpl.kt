package com.example.milsabores.data.repository

import com.example.milsabores.data.local.dao.UsuarioDao
import com.example.milsabores.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class UsuarioRepositoryImpl(
    private val dao: UsuarioDao
) : UsuarioRepository {

    // 1. Gestión de Sesión en Memoria
    private val _usuarioLogueado = MutableStateFlow<Usuario?>(null)
    override val usuarioLogueado: StateFlow<Usuario?> = _usuarioLogueado.asStateFlow()

    override suspend fun login(email: String, password: String): Usuario? {
        val usuario = dao.validarCredenciales(email, password)
        if (usuario != null) {
            _usuarioLogueado.value = usuario // ¡Guardamos la sesión!
        }
        return usuario
    }

    override fun logout() {
        _usuarioLogueado.value = null // ¡Borramos la sesión!
    }

    override suspend fun registrarUsuario(usuario: Usuario): Result<Unit> {
        return try {
            val existente = dao.obtenerUsuarioPorEmail(usuario.email)
            if (existente != null) {
                Result.failure(Exception("El correo ya existe"))
            } else {
                dao.registrarUsuario(usuario)
                // Opcional: Auto-login al registrar
                // _usuarioLogueado.value = usuario
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUsuario(usuarioId: Int): Flow<Usuario?> {
        return dao.obtenerUsuarioPorId(usuarioId)
    }
    // ...
    override suspend fun actualizarFotoPerfil(usuarioId: Int, fotoUri: String) {
        dao.actualizarFotoPerfil(usuarioId, fotoUri)
        val usuarioActualizado = dao.obtenerUsuarioPorId(usuarioId).first()

        if (_usuarioLogueado.value?.id == usuarioId) {
            val usuarioFresco = dao.validarCredenciales(_usuarioLogueado.value!!.email, _usuarioLogueado.value!!.password)
            _usuarioLogueado.value = usuarioFresco
        }
    }
}