package com.example.milsabores.data.repository

import android.util.Log
import com.example.milsabores.data.local.dao.UsuarioDao
import com.example.milsabores.data.local.entity.Usuario
import com.example.milsabores.data.remote.api.MilSaboresApi
import com.example.milsabores.data.remote.dto.UsuarioDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class UsuarioRepositoryImpl(
    private val dao: UsuarioDao,
    private val api: MilSaboresApi // <--- 1. AGREGAMOS ESTO AL CONSTRUCTOR
) : UsuarioRepository {

    // Gestión de Sesión en Memoria
    private val _usuarioLogueado = MutableStateFlow<Usuario?>(null)
    override val usuarioLogueado: StateFlow<Usuario?> = _usuarioLogueado.asStateFlow()

    /**
     * LOGIN MEJORADO:
     * Intenta autenticar con la API. Si funciona, guarda/actualiza en local.
     * Si falla (sin internet), usa los datos locales.
     */
    override suspend fun login(email: String, password: String): Usuario? {
        return try {
            // A. Preguntamos a MockAPI
            val usuariosApi = api.loginUsuario(email, password)

            if (usuariosApi.isNotEmpty()) {
                // Login exitoso en la nube
                val usuarioDto = usuariosApi[0]

                // B. Sincronizamos con Room (Base de datos local)
                val usuarioEntity = mapDtoToEntity(usuarioDto, password)

                val localExistente = dao.obtenerUsuarioPorEmail(email)
                if (localExistente == null) {
                    dao.registrarUsuario(usuarioEntity)
                }
                // (Aquí podrías agregar un 'else { dao.update(...) }' si quisieras actualizar datos)

                // C. Iniciamos sesión usando la versión Local (para tener el ID correcto de Room)
                val usuarioFinal = dao.validarCredenciales(email, password)
                _usuarioLogueado.value = usuarioFinal
                usuarioFinal
            } else {
                null // Credenciales incorrectas en API
            }
        } catch (e: Exception) {
            Log.e("UsuarioRepo", "Error login remoto: ${e.message}")
            // D. Fallback: Si no hay internet, intentamos login local
            val usuarioLocal = dao.validarCredenciales(email, password)
            if (usuarioLocal != null) {
                _usuarioLogueado.value = usuarioLocal
            }
            usuarioLocal
        }
    }

    override fun logout() {
        _usuarioLogueado.value = null
    }

    /**
     * REGISTRO MEJORADO:
     * Primero crea el usuario en la Nube (API). Si tiene éxito, lo guarda en el celular.
     */
    override suspend fun registrarUsuario(usuario: Usuario): Result<Unit> {
        return try {
            // Validación preventiva local
            val existente = dao.obtenerUsuarioPorEmail(usuario.email)
            if (existente != null) {
                return Result.failure(Exception("El correo ya existe localmente"))
            }

            // 1. Convertir a DTO y enviar a MockAPI
            val nuevoUsuarioDto = UsuarioDto(
                email = usuario.email,
                password = usuario.password,
                nombre = usuario.nombre,
                direccion = usuario.direccion ?: "",
                telefono = "" // MockAPI lo recibirá vacío por ahora
            )

            // Si esto falla (ej. sin internet), saltará al catch y no guardará en local
            api.registrarUsuario(nuevoUsuarioDto)

            // 2. Si la API respondió OK, guardamos en Room
            dao.registrarUsuario(usuario)

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("UsuarioRepo", "Error registro: ${e.message}")
            Result.failure(e)
        }
    }

    override fun getUsuario(usuarioId: Int): Flow<Usuario?> {
        return dao.obtenerUsuarioPorId(usuarioId)
    }

    override suspend fun actualizarFotoPerfil(usuarioId: Int, fotoUri: String) {
        // Mantenemos tu lógica original (solo local por ahora)
        dao.actualizarFotoPerfil(usuarioId, fotoUri)

        // Refrescar sesión si es necesario
        if (_usuarioLogueado.value?.id == usuarioId) {
            val usuarioActualizado = dao.obtenerUsuarioPorId(usuarioId).first()
            if (usuarioActualizado != null) {
                _usuarioLogueado.value = usuarioActualizado
            }
        }
    }

    // --- FUNCIÓN PRIVADA DE AYUDA ---
    private fun mapDtoToEntity(dto: UsuarioDto, passwordReal: String): Usuario {
        // Asignamos ID=0 para que Room genere su propio ID numérico
        return Usuario(
            id = 0,
            email = dto.email,
            password = passwordReal,
            nombre = dto.nombre,
            direccion = dto.direccion,
            fotoPerfil = null
        )
    }
}