package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsabores.MilSaboresApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {

        // --- De la rama 'main' (HomeScreen) ---
        initializer {
            val productoRepository =
                milSaboresApplication().container.productoRepository
            val blogRepository =
                milSaboresApplication().container.blogRepository
            HomeViewModel(
                productoRepository = productoRepository,
                blogRepository = blogRepository
            )
        }

        // --- De la rama 'catalogo' ---
        initializer {
            val productoRepository =
                milSaboresApplication().container.productoRepository
            CatalogoViewModel(
                productoRepository = productoRepository
            )
        }

        // --- De la rama 'catalogo' ---
        initializer {
            val productoRepository =
                milSaboresApplication().container.productoRepository
            val carritoRepository =
                milSaboresApplication().container.carritoRepository
            val savedStateHandle = createSavedStateHandle()
            DetalleViewModel(
                savedStateHandle = savedStateHandle,
                productoRepository = productoRepository,
                carritoRepository = carritoRepository
            )
        }

        // --- De la rama 'blog' ---
        initializer {
            val blogRepository =
                milSaboresApplication().container.blogRepository
            BlogViewModel(
                blogRepository = blogRepository
            )
        }
        //Carrito
        initializer {
            // 1. Obtenemos los DOS repositorios que necesita
            val productoRepository =
                milSaboresApplication().container.productoRepository
            val carritoRepository =
                milSaboresApplication().container.carritoRepository

            // 2. Creamos el CarritoViewModel
            CarritoViewModel(
                productoRepository = productoRepository,
                carritoRepository = carritoRepository
            )
        }
        //Checkout
        initializer {
            // 1. Obtenemos los repositorios que necesita
            val application = milSaboresApplication() // Obtenemos la app
            val container = application.container // Obtenemos el contenedor

            CheckoutViewModel(
                carritoRepository = container.carritoRepository,
                productoRepository = container.productoRepository,
                appContainer = container
            )
        }
        initializer {
            // 1. Obtenemos el contenedor
            val container = milSaboresApplication().container

            // 2. Creamos el ConfirmacionViewModel
            ConfirmacionViewModel(
                appContainer = container
            )
        }

        initializer {
            // 1. Obtén el repositorio de usuarios (¡el que acabamos de añadir!)
            val usuarioRepository =
                milSaboresApplication().container.usuarioRepository

            // 2. Crea y devuelve el LoginViewModel, pasándole el repositorio
            LoginViewModel(
                usuarioRepository = usuarioRepository
            )
        }

        // --- REGISTRO ---
        initializer {
            // 1. Obtén el repositorio de usuarios (el mismo que usa Login)
            val usuarioRepository =
                milSaboresApplication().container.usuarioRepository

            // 2. Crea y devuelve el RegistroViewModel
            // (¡Saldrá en ROJO! Lo crearemos en el Paso 2)
            RegistroViewModel(
                usuarioRepository = usuarioRepository
            )
        }
    }
}

fun CreationExtras.milSaboresApplication(): MilSaboresApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MilSaboresApplication)