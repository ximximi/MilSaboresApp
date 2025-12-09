package com.example.milsabores.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// @JsonClass le dice a Moshi que genere el adaptador automáticamente (más rápido)
@JsonClass(generateAdapter = true)
data class ProductoDto(
    // @Json(name = "...") conecta el nombre de la API con tu variable
    @Json(name = "id") val id: String, // Las APIs suelen mandar ID como String
    @Json(name = "nombre") val nombre: String,
    @Json(name = "precio") val precio: Double,
    @Json(name = "descripcion") val descripcion: String,
    @Json(name = "imagen") val imagen: String,
    @Json(name = "categoria") val categoria: String,

    // Campos opcionales (pueden no venir en la API básica de MockAPI)
    @Json(name = "codigo") val codigo: String? = "GEN-001",
    @Json(name = "icono") val icono: String? = "fa-cake"
)