package com.example.milsabores

import android.app.Application
import com.example.milsabores.di.AppContainer
import com.example.milsabores.di.AppDataContainer

/**
 * Clase Application personalizada.
 * Se crea una vez y vive mientras la app esté abierta.
 */
class MilSaboresApplication : Application() {

    // Contenedor de dependencias
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Cuando la app se crea, se construye el contenedor
        container = AppDataContainer(this)
    }
}