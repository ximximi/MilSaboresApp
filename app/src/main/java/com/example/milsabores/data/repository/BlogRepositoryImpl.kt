package com.example.milsabores.data.repository

import com.example.milsabores.data.local.dao.BlogDao
import com.example.milsabores.data.local.entity.Blog
import kotlinx.coroutines.flow.Flow

/**
 * Implementación que habla con el BlogDao.
 */
class BlogRepositoryImpl(
    private val dao: BlogDao // Pide el BlogDao
) : BlogRepository { // Cumple el contrato

    override fun obtenerTodos(): Flow<List<Blog>> {
        return dao.obtenerTodas() // Llama al DAO
    }
    override fun obtenerPorId(blogId: Int): Flow<Blog> {
        return dao.obtenerPorId(blogId)
    }
}