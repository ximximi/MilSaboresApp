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
    }
}

fun CreationExtras.milSaboresApplication(): MilSaboresApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MilSaboresApplication)