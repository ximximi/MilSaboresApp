package com.example.milsabores.di

import android.content.Context
import com.example.milsabores.data.local.DatosCompra
import com.example.milsabores.data.local.PasteleriaDatabase
import com.example.milsabores.data.repository.BlogRepository
import com.example.milsabores.data.repository.BlogRepositoryImpl
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.data.repository.CarritoRepositoryImpl
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.data.repository.ProductoRepositoryImpl
import com.example.milsabores.data.repository.UsuarioRepository
import com.example.milsabores.data.repository.UsuarioRepositoryImpl
import com.example.milsabores.data.remote.api.MilSaboresApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class AppDataContainer(
    private val context: Context
) : AppContainer {

    private val database: PasteleriaDatabase by lazy {
        PasteleriaDatabase.obtenerInstancia(context)
    }

    // A. La URL Base
    private val baseUrl = "https://692e22c591e00bafccd2f95a.mockapi.io/api/v1/"

    // B. Moshi (El traductor de JSON)
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // C. Logging Interceptor (Para ver en consola qué datos llegan)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // D. Cliente OkHttp (El "navegador" invisible)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        // .addInterceptor(AuthInterceptor()) // (Aquí iría el Auth si tuvieras token real)
        .connectTimeout(30, TimeUnit.SECONDS) // Espera máx 30 seg
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // E. Retrofit (El constructor de la API)
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

    // F. El Servicio API (La interfaz que creamos antes)
    private val retrofitService: MilSaboresApi by lazy {
        retrofit.create(MilSaboresApi::class.java)
    }
    //Repositorios
    override val productoRepository: ProductoRepository by lazy {
        ProductoRepositoryImpl(
            database.productoDao(),
            api = retrofitService
        )
    }

    // --- De la rama 'catalogo' ---
    override val carritoRepository: CarritoRepository by lazy {
        CarritoRepositoryImpl(database.carritoDao())
    }

    // --- De la rama 'blog' ---
    override val blogRepository: BlogRepository by lazy {
        BlogRepositoryImpl(database.blogDao())
    }

    override var ultimaCompra: DatosCompra? = null

    override val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepositoryImpl(database.usuarioDao())
    }
}