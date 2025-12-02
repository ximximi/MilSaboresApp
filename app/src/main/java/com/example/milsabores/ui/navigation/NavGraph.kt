package com.example.milsabores.ui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.milsabores.ui.screen.*
import com.example.milsabores.ui.viewmodel.AppViewModelProvider
import com.example.milsabores.ui.viewmodel.LoginViewModel
import com.example.milsabores.ui.viewmodel.PerfilViewModel
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
                onLoginClick = { navController.navigate(Rutas.LOGIN) },
                onRegistroClick = { navController.navigate(Rutas.REGISTRO) },
                onInvitadoClick = {
                    // Navega al Home como invitado
                    navController.navigate(Rutas.HOME)
                }
            )
        }

        // --- RUTA DEL HOME ---
        composable(Rutas.HOME) {
            HomeScreen(
                navController = navController,
                onVolverClick = { navController.popBackStack() },
                onCarritoClick = { navController.navigate(Rutas.CARRITO) },
                onVerCatalogoClick = { navController.navigate(Rutas.CATALOGO) },
                onBlogClick = { navController.navigate(Rutas.BLOG) },
                onPerfilClick = { navController.navigate(Rutas.PERFIL) },
                onProductoClick = { productoId ->
                    navController.navigate(Rutas.irADetalle(productoId))
                }
            )
        }

        // --- RUTA DE LOGIN ---
        composable(Rutas.LOGIN) {
            val loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.INDEX) { inclusive = true }
                    }
                },
                onNavigateToRegistro = {
                    navController.navigate(Rutas.REGISTRO)
                },
                onVolverAIndex = { navController.popBackStack() }
            )
        }

        // --- REGISTRO ---
        composable(Rutas.REGISTRO) {
            val viewModel: RegistroViewModel = viewModel(factory = AppViewModelProvider.Factory)
            RegistroScreen(
                viewModel = viewModel,
                onRegistroSuccess = {
                    navController.popBackStack()
                },
                onVolverALogin = {
                    navController.popBackStack()
                }
            )
        }

        // --- PERFIL (NUEVO) ---
        // --- RUTA PERFIL (Lógica Corregida) ---
        composable(Rutas.PERFIL) {
            val perfilViewModel: PerfilViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val usuario by perfilViewModel.usuarioLogueado.collectAsState()
            // No navegamos a otra ruta. Decidimos qué mostrar AQUÍ mismo.
            if (usuario != null) {
                // CASO A: Usuario Logueado -> Mostrar Perfil
                PerfilScreen(
                    onVolverClick = { navController.popBackStack() },
                    onCerrarSesion = {
                        navController.navigate(Rutas.INDEX) {
                            popUpTo(0)
                        }
                    }
                )
            } else {
                // CASO B: Usuario No Logueado -> Mostrar Login
                // Reutilizamos la pantalla de Login, pero dentro de la ruta Perfil
                val loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = {

                    },
                    onNavigateToRegistro = { navController.navigate(Rutas.REGISTRO) },
                    onVolverAIndex = { navController.popBackStack() }
                )
            }
        }

        // --- CATALOGO ---
        composable(Rutas.CATALOGO) {
            CatalogoScreen(
                onVolverClick = { navController.popBackStack() },
                onCarritoClick = { navController.navigate(Rutas.CARRITO) },
                onPerfilClick = { navController.navigate(Rutas.PERFIL) }, // <-- AGREGADO
                onProductoClick = { productoId ->
                    navController.navigate(Rutas.irADetalle(productoId))
                }
            )
        }

        // --- DETALLE ---
        composable(
            route = Rutas.DETALLE_PRODUCTO,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) {
            DetalleScreen(
                onVolverClick = { navController.popBackStack() },
                onPerfilClick = { navController.navigate(Rutas.PERFIL) },
                onCarritoClick = { navController.navigate(Rutas.CARRITO) } // <-- ¡AÑADIDO!
            )
        }

        // --- CARRITO ---
        composable(Rutas.CARRITO) {
            CarritoScreen(
                onVolverClick = { navController.popBackStack() },
                onIrAPagarClick = { navController.navigate(Rutas.CHECKOUT) },
                onPerfilClick = { navController.navigate(Rutas.PERFIL) } // <-- AGREGADO
            )
        }

        // --- CHECKOUT ---
        composable(Rutas.CHECKOUT) {
            CheckoutScreen(
                onVolverClick = { navController.popBackStack() },
                onPagoExitoso = {
                    navController.navigate(Rutas.CONFIRMACION) {
                        popUpTo(Rutas.HOME) { inclusive = false }
                    }
                }
            )
        }

        // --- CONFIRMACION ---
        composable(Rutas.CONFIRMACION) {
            ConfirmacionScreen(
                onVolverAlInicioClick = {
                    navController.navigate(Rutas.INDEX) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                onSeguirComprandoClick = {
                    navController.navigate(Rutas.CATALOGO) {
                        popUpTo(Rutas.HOME) { inclusive = false }
                    }
                }
            )
        }

        // --- BLOG ---
        composable(Rutas.BLOG) {
            BlogScreen(
                navController = navController,
                onVolverClick = { navController.popBackStack() },
                onPerfilClick = { navController.navigate(Rutas.PERFIL) },
                onCarritoClick = { navController.navigate(Rutas.CARRITO) } // <-- ¡AÑADIDO!
            )
        }

        composable(
            route = Rutas.DETALLE_BLOG,
            arguments = listOf(navArgument("blogId") { type = NavType.IntType })
        ) {
            DetalleBlogScreen(
                onVolverClick = { navController.popBackStack() }
            )
        }

        composable(Rutas.ADMIN_LOGIN) {
            Text(
                "Pantalla de Login de Admin (en construcción)",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}