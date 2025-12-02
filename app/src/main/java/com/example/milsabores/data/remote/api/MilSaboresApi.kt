package com.example.milsabores.data.remote.api

import com.example.milsabores.data.remote.dto.ProductoDto
import retrofit2.http.GET

interface MilSaboresApi {

    // Obtener todos los productos
    // La ruta "productos" se suma a tu URL base
    @GET("productos")
    suspend fun obtenerProductos(): List<ProductoDto>
}