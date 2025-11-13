package com.example.milsabores.ui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.milsabores.ui.screen.HomeScreen
import com.example.milsabores.ui.screen.IndexScreen
import com.example.milsabores.ui.screen.CatalogoScreen
import com.example.milsabores.ui.screen.DetalleScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun NavGraph(
    navController: NavHostController
) {
    // NavHost es el componente que intercambia las pantallas
    NavHost(
        navController = navController,
        startDestination = Rutas.INDEX // La app parte en la pantalla de bienvenida
    ) {

        // --- RUTAS DE CLIENTE ---

        composable(Rutas.INDEX) {
            // ¡Esta ruta ahora está activa!
            IndexScreen(
                // Le decimos qué hacer al hacer clic en "Entrar"
                onEntrarClick = { navController.navigate(Rutas.HOME) },

                // Le decimos qué hacer al hacer clic en "Admin"
                onAdminClick = { navController.navigate(Rutas.ADMIN_LOGIN) }
            )
        }

        composable(Rutas.HOME) {
            // HomeScreen(navController = navController) // <-- Lo activaremos luego
        }

        composable(Rutas.HOME) {
            HomeScreen(
                onVolverClick = { navController.popBackStack() }, // Vuelve a la pantalla anterior (Index)
                onCarritoClick = { navController.navigate(Rutas.CARRITO) },
                onVerCatalogoClick = { navController.navigate(Rutas.CATALOGO) }
            )
        }

        composable(Rutas.CARRITO) {
            Text("Pantalla de Carrito (en construcción)")
        }

        composable(Rutas.CHECKOUT) {
            // CheckoutScreen(navController = navController)
        }

        composable(Rutas.CONFIRMACION) {
            // ConfirmacionScreen(navController = navController)
        }

        composable(Rutas.BLOG) {
            // BlogScreen(navController = navController)
        }

        composable(Rutas.CATALOGO) {
            CatalogoScreen(
                onVolverClick = { navController.popBackStack() },
                onCarritoClick = { navController.navigate(Rutas.CARRITO) },
                onProductoClick = { productoId ->
                    // Navegamos al detalle, pasando el ID del producto
                    navController.navigate(Rutas.irADetalle(productoId))
                }
            )
        }

        composable(
            route = Rutas.DETALLE_PRODUCTO, // Ruta: "detalle_producto/{productoId}"
            arguments = listOf(navArgument("productoId") { // 1. Define el argumento
                type = NavType.IntType // 2. Dice que el ID es un número
            })
        ) {
            // 3. El ViewModel se crea automáticamente con el ID
            DetalleScreen(
                onVolverClick = { navController.popBackStack() } // Vuelve a la pantalla anterior
            )
        }

        // --- RUTAS DE AUTENTICACIÓN ---

        composable(Rutas.LOGIN) {
            // LoginScreen(navController = navController)
        }
        composable(Rutas.ADMIN_LOGIN) {
            // Aquí irá tu LoginAdminScreen
            Text("Pantalla de Login de Admin (en construcción)",
                color = MaterialTheme.colorScheme.primary)
        }
        composable(Rutas.REGISTRO) {
            // RegistroScreen(navController = navController)
        }

        // --- RUTAS DE ADMIN ---
        // (Añadiremos estas más adelante)

    }
}