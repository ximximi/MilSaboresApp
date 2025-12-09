package com.example.milsabores.ui.viewmodel

import com.example.milsabores.data.local.entity.Producto

/**
 * Clase compartida que representa un item del carrito
 * con todos los detalles del producto.
 */
data class ItemCarritoDetallado(
    val producto: Producto,
    val cantidad: Int
)