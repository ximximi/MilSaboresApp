package com.example.milsabores.ui.navigation

// Usamos un 'object' para agrupar todas nuestras rutas como constantes
object Rutas {
    // Rutas de Clientes
    const val INDEX = "index" // Pantalla de bienvenida (index.html)
    const val HOME = "home" // Catálogo principal (catalogo.html)
    const val DETALLE_PRODUCTO = "detalle_producto/{productoId}" // (detalle-producto.html)
    const val CARRITO = "carrito" // (carrito.html)
    const val CHECKOUT = "checkout" // (checkout.html)
    const val CONFIRMACION = "confirmacion" // (confirmacion.html)
    const val BLOG = "blog" // (blog.html)
    const val CATALOGO = "catalogo"

    // Rutas de Autenticación
    const val LOGIN = "login" // (login.html)
    const val REGISTRO = "registro" // (registro-usuario.html)

    // Rutas de Administrador
    const val ADMIN_LOGIN = "admin_login" // Usaremos login.html para admin tmb
    const val ADMIN_HOME = "admin_home" // (home-admin.html)
    const val ADMIN_GESTION_PRODUCTOS = "admin_gestion_productos" // (gestion-productos.html)
    const val ADMIN_CREAR_PRODUCTO = "admin_crear_producto" // (create-producto.html)
    const val ADMIN_GESTION_PEDIDOS = "admin_gestion_pedidos" // (gestion-pedidos.html)
    const val DETALLE_BLOG = "detalle_blog/{blogId}"
    // Función para navegar a un producto con su ID
    fun irADetalle(productoId: Int) = "detalle_producto/$productoId"
    fun irADetalleBlog(blogId: Int) = "detalle_blog/$blogId"
}