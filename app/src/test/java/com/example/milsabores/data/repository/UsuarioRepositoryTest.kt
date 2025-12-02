package com.example.milsabores.data.repository

import com.example.milsabores.data.local.dao.UsuarioDao
import com.example.milsabores.data.local.entity.Usuario
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
    private val repository = UsuarioRepositoryImpl(usuarioDao)

    // --- AQUÍ ESTABA EL ERROR ---
    // Usamos argumentos nombrados para evitar confusión con el orden
    private val usuarioFake = Usuario(
        id = 1,
        nombre = "Juan",
        email = "juan@mail.com",
        password = "1234",
        direccion = "Calle Falsa 123",
        fotoPerfil = null
        // rol = "cliente" // <-- NOTA: Si tu entity 'Usuario' tiene 'rol', descomenta esto.
        // Si no lo tiene, déjalo así.
    )

    @Test
    fun `login exitoso devuelve usuario y actualiza estado`() = runTest {
        // GIVEN
        whenever(usuarioDao.validarCredenciales("juan@mail.com", "1234")).thenReturn(usuarioFake)

        // WHEN
        val resultado = repository.login("juan@mail.com", "1234")

        // THEN
        assertNotNull(resultado)
        assertEquals("Juan", resultado?.nombre)
        // Verificamos que el StateFlow en memoria se actualizó
        assertEquals(usuarioFake, repository.usuarioLogueado.value)
    }

    @Test
    fun `login fallido devuelve null`() = runTest {
        // GIVEN
        whenever(usuarioDao.validarCredenciales("malo@mail.com", "0000")).thenReturn(null)

        // WHEN
        val resultado = repository.login("malo@mail.com", "0000")

        // THEN
        assertNull(resultado)
    }

    @Test
    fun `registrarUsuario tiene exito si el email no existe`() = runTest {
        // GIVEN
        whenever(usuarioDao.obtenerUsuarioPorEmail("nuevo@mail.com")).thenReturn(null)

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
        // Verificamos que llamó al DAO para guardar
        verify(usuarioDao).registrarUsuario(nuevoUsuario)
    }

    @Test
    fun `registrarUsuario falla si el email ya existe`() = runTest {
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
        assertEquals("El correo ya existe", resultado.exceptionOrNull()?.message)

        verify(usuarioDao, never()).registrarUsuario(any())
    }
}