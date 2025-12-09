package com.example.milsabores.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UsuarioDto(
    // El ID es opcional al enviarlo (null), pero MockAPI siempre devuelve uno.
    @Json(name = "id") val id: String? = null,

    @Json(name = "nombre") val nombre: String,
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,

    // Campos que definimos como opcionales o vacíos al inicio
    @Json(name = "direccion") val direccion: String? = "",
    @Json(name = "telefono") val telefono: String? = "",
)