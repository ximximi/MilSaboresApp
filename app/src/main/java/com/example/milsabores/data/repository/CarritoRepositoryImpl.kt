package com.example.milsabores.data.repository

import com.example.milsabores.data.local.dao.CarritoDao
import com.example.milsabores.data.local.entity.ItemCarrito
import com.example.milsabores.data.local.entity.Producto
import kotlinx.coroutines.flow.Flow

/**
 * Implementación que habla con el CarritoDao.
 */
class CarritoRepositoryImpl(
    private val dao: CarritoDao
) : CarritoRepository {

    override fun obtenerItems(): Flow<List<ItemCarrito>> {
        return dao.obtenerItems()
    }

    override suspend fun agregarAlCarrito(producto: Producto, cantidad: Int) {
        // Revisa si el producto YA está en el carrito
        val itemExistente = dao.obtenerItemPorId(producto.id)

        if (itemExistente != null) {
            // Si existe, actualiza la cantidad
            val nuevoItem = itemExistente.copy(
                cantidad = itemExistente.cantidad + cantidad
            )
            dao.insertar(nuevoItem)
        } else {
            // Si no existe, crea un nuevo item
            val nuevoItem = ItemCarrito(
                productoId = producto.id,
                cantidad = cantidad
            )
            dao.insertar(nuevoItem)
        }
    }

    override suspend fun eliminarDelCarrito(productoId: Int) {
        val itemExistente = dao.obtenerItemPorId(productoId)
        if (itemExistente != null) {
            dao.eliminar(itemExistente)
        }
    }
}