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
        // (Por ejemplo, para CarritoViewModel, LoginViewModel, etc.)

    }
}

/**
 * Función de extensión para obtener la instancia de nuestra Application
 * desde dentro de la fábrica.
 */
fun CreationExtras.milSaboresApplication(): MilSaboresApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MilSaboresApplication)
