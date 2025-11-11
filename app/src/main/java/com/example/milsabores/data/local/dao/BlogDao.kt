package com.example.milsabores.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.milsabores.data.local.entity.Blog
import kotlinx.coroutines.flow.Flow

@Dao
interface BlogDao {

    // Inserta una lista de entradas de blog.
    // Si una entrada ya existe (mismo ID), la reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(blogs: List<Blog>)

    // Obtiene TODAS las entradas del blog, ordenadas por fecha (a futuro)
    // Por ahora, solo las obtenemos todas.
    @Query("SELECT * FROM blog")
    fun obtenerTodas(): Flow<List<Blog>>

    // (Podríamos añadir un obtenerPorId si tuvieras una pantalla de detalle de blog)
}