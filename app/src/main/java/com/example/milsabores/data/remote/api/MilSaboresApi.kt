package com.example.milsabores.data.remote.api

import com.example.milsabores.data.remote.dto.ProductoDto
import com.example.milsabores.data.remote.dto.UsuarioDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MilSaboresApi {

    // --- PRODUCTOS ---

    // 1. Obtener producto individual (Detalle)
    @GET("productos/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: String): ProductoDto

    /**
     * 2. BÚSQUEDA AVANZADA (Esta reemplaza a obtenerProductos y obtenerPorCategoria)
     * Permite filtrar y ordenar todo en una sola llamada.
     * Todos los parámetros son opcionales (nullable), así que puedes usar la misma función
     * para traer todo o para filtrar.
     */
    @GET("productos")
    suspend fun obtenerProductos(
        @Query("categoria") categoria: String? = null,
        @Query("nombre") nombre: String? = null,  // MockAPI buscará coincidencia exacta o parcial según config
        @Query("sortBy") ordenarPor: String? = null, // Escribiremos "precio" aquí
        @Query("order") orden: String? = null        // "asc" (menor a mayor) o "desc" (mayor a menor)
    ): List<ProductoDto>


    // --- USUARIOS ---
    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: UsuarioDto): UsuarioDto

    @GET("usuarios")
    suspend fun loginUsuario(
        @Query("email") email: String,
        @Query("password") pass: String
    ): List<UsuarioDto>
}