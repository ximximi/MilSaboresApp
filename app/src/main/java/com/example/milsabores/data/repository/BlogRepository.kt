package com.example.milsabores.data.repository

import com.example.milsabores.data.local.entity.Blog
import kotlinx.coroutines.flow.Flow

/**
 * Contrato para el Repositorio del Blog.
 */
interface BlogRepository {

    // Contrato: "Cualquiera puede pedirme todas las entradas del blog"
    fun obtenerTodos(): Flow<List<Blog>>
    fun obtenerPorId(blogId: Int): Flow<Blog>
}