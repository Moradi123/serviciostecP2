package com.example.serviciostec.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.serviciostec.ui.screen.*
import com.example.serviciostec.viewmodel.FormularioServicioViewModel
import com.example.serviciostec.viewmodel.UserViewModel
import com.example.serviciostec.viewmodel.VehiculoViewModel
import com.example.serviciostec.viewmodel.ProductoViewModel


@Composable
fun AppNavigation(
    formViewModel: FormularioServicioViewModel,
    userViewModel: UserViewModel,
    vehiculoViewModel: VehiculoViewModel,
    productoViewModel: ProductoViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController, userViewModel)
        }

        composable("home") {
            HomeScreen(navController, formViewModel)
        }

        composable("services") {
            ServicesListScreen(navController, productoViewModel)
        }

        composable("profile") {
            ProfileScreen(navController, userViewModel)
        }

        composable("appointments") {
            AppointmentsScreen(navController, formViewModel)
        }

        composable("mis_vehiculos") {
            MisVehiculosScreen(navController, vehiculoViewModel, userViewModel)
        }
        composable("cart") {
            CartScreen(navController, productoViewModel)
        }

        composable(
            route = "agendar_fecha/{serviceName}",
            arguments = listOf(navArgument("serviceName") { type = NavType.StringType })
        ) { backStackEntry ->
            val serviceName = backStackEntry.arguments?.getString("serviceName") ?: "Servicio"
            ScheduleServiceScreen(navController, serviceName)
        }

        composable(
            route = "confirmar_reserva/{serviceName}/{fecha}",
            arguments = listOf(
                navArgument("serviceName") { type = NavType.StringType },
                navArgument("fecha") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
            val fecha = backStackEntry.arguments?.getString("fecha") ?: ""

            FormularioServicioScreen(
                navController = navController,
                viewModel = formViewModel,
                servicioInicial = serviceName,
                fechaInicial = fecha
            )
        }

        composable("agendar_servicio") {
            FormularioServicioScreen(navController, formViewModel)
        }
    }
}