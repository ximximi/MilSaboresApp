package com.example.milsabores.data.repository

import com.example.milsabores.data.local.dao.UsuarioDao
import com.example.milsabores.data.local.entity.Usuario
import com.example.milsabores.data.remote.api.MilSaboresApi
import com.example.milsabores.data.remote.dto.UsuarioDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UsuarioRepositoryTest {

    private val usuarioDao: UsuarioDao = mock()
    // 1. Creamos el mock de la API
    private val api: MilSaboresApi = mock()

    // 2. Se lo pasamos al constructor
    private val repository = UsuarioRepositoryImpl(usuarioDao, api)

    private val usuarioFake = Usuario(
        id = 1,
        nombre = "Juan",
        email = "juan@mail.com",
        password = "1234",
        direccion = "Calle Falsa 123",
        fotoPerfil = null
    )

    @Test
    fun loginExitosoDevuelveUsuario_y_Actualiza_Estado() = runTest {
        // GIVEN
        // Simulamos que la BD Local tiene al usuario
        whenever(usuarioDao.validarCredenciales("juan@mail.com", "1234")).thenReturn(usuarioFake)

        // TRUCO: Simulamos que NO hay internet (la API falla) para obligar al repositorio
        // a usar la lógica de "Fallback" y buscar en la base de datos local (que es lo que queremos probar aquí).
        whenever(api.loginUsuario(any(), any())).thenThrow(RuntimeException("Sin internet"))

        // WHEN
        val resultado = repository.login("juan@mail.com", "1234")

        // THEN
        assertNotNull(resultado)
        assertEquals("Juan", resultado?.nombre)
        assertEquals(usuarioFake, repository.usuarioLogueado.value)
    }

    @Test
    fun loginFallidoDevuelveNull() = runTest {
        // GIVEN
        whenever(usuarioDao.validarCredenciales("malo@mail.com", "0000")).thenReturn(null)
        // También simulamos fallo de red para probar la lógica local pura
        whenever(api.loginUsuario(any(), any())).thenThrow(RuntimeException("Sin internet"))

        // WHEN
        val resultado = repository.login("malo@mail.com", "0000")

        // THEN
        assertNull(resultado)
    }

    @Test
    fun registrarUsuario_Tiene_Exito_SiEl_email_NoExiste () = runTest {
        // GIVEN (Local)
        whenever(usuarioDao.obtenerUsuarioPorEmail("nuevo@mail.com")).thenReturn(null)

        // GIVEN (API)
        // Para que el registro local ocurra, la API debe responder "OK" primero.
        // Simulamos que la API devuelve un usuario creado exitosamente.
        val dtoDummy = UsuarioDto(id="1", nombre="Nuevo", email="nuevo@mail.com", password="1234")
        whenever(api.registrarUsuario(any())).thenReturn(dtoDummy)

        val nuevoUsuario = Usuario(
            id = 0,
            nombre = "Nuevo",
            email = "nuevo@mail.com",
            password = "1234",
            direccion = null,
            fotoPerfil = null
        )

        // WHEN
        val resultado = repository.registrarUsuario(nuevoUsuario)

        // THEN
        assertTrue(resultado.isSuccess)
        verify(usuarioDao).registrarUsuario(nuevoUsuario)
    }

    @Test
    fun registrarUsuario_Falla_SiEl_Email_Existe() = runTest {
        // GIVEN
        whenever(usuarioDao.obtenerUsuarioPorEmail("juan@mail.com")).thenReturn(usuarioFake)

        val usuarioDuplicado = Usuario(
            id = 0,
            nombre = "Otro Juan",
            email = "juan@mail.com",
            password = "1234",
            direccion = null,
            fotoPerfil = null
        )

        // WHEN
        val resultado = repository.registrarUsuario(usuarioDuplicado)

        // THEN
        assertTrue(resultado.isFailure)
        assertEquals("El correo ya existe localmente", resultado.exceptionOrNull()?.message)

        verify(usuarioDao, never()).registrarUsuario(any())
    }
}