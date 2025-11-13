package com.example.milsabores.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.milsabores.MilSaboresApplication

/**
 * Esta es nuestra Fábrica (Factory) personalizada para TODOS los ViewModels de la app.
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // --- AÑADIMOS UN "INICIALIZADOR" PARA HomeViewModel ---
        // Le dice a la app: "Cuando alguien pida un HomeViewModel..."
        initializer {
            // 1. Obtén el repositorio de productos
            val productoRepository =
                milSaboresApplication().container.productoRepository

            // 2. Crea y devuelve el HomeViewModel, pasándole el repositorio
            HomeViewModel(
                productoRepository = productoRepository
            )
        }

        // --- AQUÍ AÑADIREMOS MÁS INICIALIZADORES ---
        initializer {
            // 1. Obtenemos el mismo repositorio
            val productoRepository =
                milSaboresApplication().container.productoRepository

            // 2. Creamos el CatalogoViewModel
            CatalogoViewModel(
                productoRepository = productoRepository
            )
        }

        initializer {
            // 1. Obtenemos los repositorios que necesita
            val productoRepository =
                milSaboresApplication().container.productoRepository
            val carritoRepository =
                milSaboresApplication().container.carritoRepository

            // 2. Obtenemos el SavedStateHandle (para el ID)
            val savedStateHandle = createSavedStateHandle()

            // 3. Creamos el DetalleViewModel
            DetalleViewModel(
                savedStateHandle = savedStateHandle,
                productoRepository = productoRepository,
                carritoRepository = carritoRepository
            )
        }

    }
}

/**
 * Función de extensión para obtener la instancia de nuestra Application
 * desde dentro de la fábrica.
 */
fun CreationExtras.milSaboresApplication(): MilSaboresApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MilSaboresApplication)
