package com.example.milsabores.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.milsabores.data.local.entity.ItemCarrito
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {

    // Inserta un item. Si ya existe, reemplaza (actualiza la cantidad)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: ItemCarrito)

    // Obtiene todos los items del carrito
    @Query("SELECT * FROM carrito")
    fun obtenerItems(): Flow<List<ItemCarrito>>

    // Elimina un item del carrito
    @Delete
    suspend fun eliminar(item: ItemCarrito)

    // Vacía toda la tabla del carrito
    @Query("DELETE FROM carrito")
    suspend fun limpiarCarrito()

    // Obtiene un item por su ID (para ver si ya existe)
    @Query("SELECT * FROM carrito WHERE productoId = :productoId")
    suspend fun obtenerItemPorId(productoId: Int): ItemCarrito?
}