package com.example.milsabores.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.milsabores.data.local.entity.Producto
import kotlinx.coroutines.flow.Flow // Importante para datos "reactivos"

@Dao // <-- Le dice a Room que esto es un DAO
interface ProductoDao {

    // Inserta una lista de productos.
    // Si un producto ya existe (mismo ID), lo reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(productos: List<Producto>)

    // Obtiene TODOS los productos de la tabla
    // Flow<> hace que la lista se actualice sola si hay cambios.
    @Query("SELECT * FROM productos")
    fun obtenerTodos(): Flow<List<Producto>>

    // Obtiene un producto específico por su ID
    @Query("SELECT * FROM productos WHERE id = :productoId")
    fun obtenerPorId(productoId: Int): Flow<Producto>

    // Obtiene solo los productos de una categoría específica
    @Query("SELECT * FROM productos WHERE categoria = :categoria")
    fun obtenerPorCategoria(categoria: String): Flow<List<Producto>>
}