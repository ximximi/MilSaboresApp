package com.example.milsabores.ui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.milsabores.ui.screen.CarritoScreen
import com.example.milsabores.ui.screen.BlogScreen
import com.example.milsabores.ui.screen.CatalogoScreen
import com.example.milsabores.ui.screen.CheckoutScreen
import com.example.milsabores.ui.screen.ConfirmacionScreen
import com.example.milsabores.ui.screen.DetalleScreen
import com.example.milsabores.ui.screen.HomeScreen
import com.example.milsabores.ui.screen.IndexScreen
import com.example.milsabores.ui.screen.LoginScreen
import com.example.milsabores.ui.screen.RegistroScreen
import com.example.milsabores.ui.viewmodel.AppViewModelProvider
import com.example.milsabores.ui.viewmodel.LoginViewModel
import com.example.milsabores.ui.viewmodel.RegistroViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.INDEX,
        modifier = modifier
    ) {

        // --- RUTA DE BIENVENIDA ---

        composable(Rutas.INDEX) {
            IndexScreen(
                onEntrarClick = { navController.navigate(Rutas.HOME) },
                onAdminClick = { navController.navigate(Rutas.LOGIN) }
            )
        }

        // --- RUTA DEL HOME ---
        composable(Rutas.HOME) {
            HomeScreen(
                onVolverClick = { navController.popBackStack() },
                onCarritoClick = { navController.navigate(Rutas.CARRITO) },
                onVerCatalogoClick = { navController.navigate(Rutas.CATALOGO) },
                onBlogClick = { navController.navigate(Rutas.BLOG) },
                onProductoClick = { productoId ->
                    navController.navigate(Rutas.irADetalle(productoId))
                }
            )
        }

        // --- RUTA DE LOGIN ---

        composable(Rutas.LOGIN) {
            // Pide el LoginViewModel usando la Fábrica Central
            val loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
            LoginScreen(
                viewModel = loginViewModel, // Pasa el ViewModel a la pantalla
                onLoginSuccess = {
                    // Si el login es exitoso, navega a Home y
                    // limpia el "historial" para que no pueda "volver" al login
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.INDEX) { inclusive = true }
                    }
                },
                onNavigateToRegistro = {
                    navController.navigate(Rutas.REGISTRO)
                }
            )
        }

        // --- REGISTRO ---
        composable(Rutas.REGISTRO) {
            // ¡Esta es la versión BUENA!
            val viewModel: RegistroViewModel = viewModel(factory = AppViewModelProvider.Factory)
            RegistroScreen(
                viewModel = viewModel,
                onRegistroSuccess = {
                    navController.popBackStack() // Vuelve a la pantalla de Login
                },
                onVolverALogin = {
                    navController.popBackStack() // Vuelve a la pantalla de Login
                }
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
            CarritoScreen(
                onVolverClick = { navController.popBackStack() },
                onIrAPagarClick = { navController.navigate(Rutas.CHECKOUT) } // Navega al Checkout
            )
        }

        composable(Rutas.CHECKOUT) {
            CheckoutScreen(
                onVolverClick = { navController.popBackStack() },
                onPagoExitoso = {
                    // Navega a la confirmación y limpia toda la pila de "compra"
                    navController.navigate(Rutas.CONFIRMACION) {
                        popUpTo(Rutas.HOME) { inclusive = false }
                    }
                }
            )
        }

        composable(Rutas.CONFIRMACION) {
            ConfirmacionScreen(
                onVolverAlInicioClick = {
                    // Navega al inicio y limpia TODA la pila de navegación
                    navController.navigate(Rutas.INDEX) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onSeguirComprandoClick = {
                    // Navega al catálogo y limpia la pila de "compra"
                    navController.navigate(Rutas.CATALOGO) {
                        popUpTo(Rutas.HOME) { inclusive = false }
                    }
                }
            )
        }

        // --- De la rama 'blog' ---
        composable(Rutas.BLOG) {
            BlogScreen(
                onVolverClick = { navController.popBackStack() }
            )
        }

        // --- RUTAS DE AUTENTICACIÓN (ADMIN) ---

        composable(Rutas.ADMIN_LOGIN) {
            Text(
                "Pantalla de Login de Admin (en construcción)",
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}