package com.example.milsabores

import com.example.milsabores.data.local.entity.Usuario
import com.example.milsabores.data.repository.UsuarioRepository
import com.example.milsabores.ui.viewmodel.LoginEvent
import com.example.milsabores.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val usuarioRepository: UsuarioRepository = mock()

    @Test
    fun `login exitoso actualiza estado a loginExitoso`() = runTest {
        // GIVEN: El repositorio devuelve un Usuario real (simulando éxito)
        // Nota: Ajusta los parámetros del Usuario si tu Entity pide más cosas (como dirección)
        val usuarioFake = Usuario(
            id = 1,
            nombre = "Juan",
            email = "juan@mail.com",
            password = "1234",
            direccion = "Calle Falsa",
            fotoPerfil = null
        )

        whenever(usuarioRepository.login("juan@mail.com", "1234"))
            .thenReturn(usuarioFake)

        val viewModel = LoginViewModel(usuarioRepository)

        // Simulamos que el usuario escribe
        viewModel.onLoginEvent(LoginEvent.OnEmailChange("juan@mail.com"))
        viewModel.onLoginEvent(LoginEvent.OnPasswordChange("1234"))

        // WHEN: Clic en login
        viewModel.onLoginEvent(LoginEvent.OnLoginClick)

        // THEN: Verificamos éxito
        assertTrue(viewModel.uiState.value.loginExitoso)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `login fallido muestra mensaje de error`() = runTest {
        // GIVEN: El repositorio devuelve null (simulando credenciales incorrectas)
        whenever(usuarioRepository.login("malo@mail.com", "0000"))
            .thenReturn(null)

        val viewModel = LoginViewModel(usuarioRepository)
        viewModel.onLoginEvent(LoginEvent.OnEmailChange("malo@mail.com"))
        viewModel.onLoginEvent(LoginEvent.OnPasswordChange("0000"))

        // WHEN
        viewModel.onLoginEvent(LoginEvent.OnLoginClick)

        // THEN
        assertFalse(viewModel.uiState.value.loginExitoso)
        // Verificamos el mensaje que tu compañera puso en su ViewModel
        assertEquals("Credenciales incorrectas", viewModel.uiState.value.error)
    }

    @Test
    fun `validacion impide login con campos vacios`() = runTest {
        val viewModel = LoginViewModel(usuarioRepository)
        // No llenamos nada (email y pass vacíos)

        // WHEN
        viewModel.onLoginEvent(LoginEvent.OnLoginClick)

        // THEN
        // Verificamos el mensaje de validación del ViewModel
        assertEquals("Email y contraseña no pueden estar vacíos", viewModel.uiState.value.error)
    }
}