package com.example.milsabores.ui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.milsabores.ui.screen.BlogScreen
import com.example.milsabores.ui.screen.CatalogoScreen
import com.example.milsabores.ui.screen.DetalleScreen
import com.example.milsabores.ui.screen.HomeScreen
import com.example.milsabores.ui.screen.IndexScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.INDEX
    ) {

        // --- RUTAS DE CLIENTE ---
        composable(Rutas.INDEX) {
            IndexScreen(
                onEntrarClick = { navController.navigate(Rutas.HOME) },
                onAdminClick = { navController.navigate(Rutas.ADMIN_LOGIN) }
            )
        }

        composable(Rutas.HOME) {
            HomeScreen(
                onVolverClick = { navController.popBackStack() },
                onCarritoClick = { navController.navigate(Rutas.CARRITO) },
                onVerCatalogoClick = { navController.navigate(Rutas.CATALOGO) }
            )
        }

        // --- De la rama 'catalogo' ---
        composable(Rutas.CATALOGO) {
            CatalogoScreen(
                onVolverClick = { navController.popBackStack() },
                onCarritoClick = { navController.navigate(Rutas.CARRITO) },
                onProductoClick = { productoId ->
                    navController.navigate(Rutas.irADetalle(productoId))
                }
            )
        }

        // --- De la rama 'catalogo' ---
        composable(
            route = Rutas.DETALLE_PRODUCTO,
            arguments = listOf(navArgument("productoId") {
                type = NavType.IntType
            })
        ) {
            DetalleScreen(
                onVolverClick = { navController.popBackStack() }
            )
        }

        composable(Rutas.CARRITO) {
            Text("Pantalla de Carrito (en construcción)")
        }

        composable(Rutas.CHECKOUT) {
            // ...
        }

        composable(Rutas.CONFIRMACION) {
            // ...
        }

        // --- De la rama 'blog' ---
        composable(Rutas.BLOG) {
            BlogScreen(
                onVolverClick = { navController.popBackStack() }
            )
        }

        // --- RUTAS DE AUTENTICACIÓN ---
        composable(Rutas.LOGIN) {
            // ...
        }
        composable(Rutas.ADMIN_LOGIN) {
            Text(
                "Pantalla de Login de Admin (en construcción)",
                color = MaterialTheme.colorScheme.primary
            )
        }
        composable(Rutas.REGISTRO) {
            // ...
        }
    }
}