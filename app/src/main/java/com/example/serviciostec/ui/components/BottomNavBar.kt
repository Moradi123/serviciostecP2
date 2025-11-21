package com.example.serviciostec.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // <--- IMPORTANTE: Para que funcione el 'by'
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState // <--- IMPORTANTE: Para saber en qué pantalla estás

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        // 1. Inicio
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    navController.navigate("home") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )

        // 2. Citas (Historial)
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Citas") },
            label = { Text("Citas") },
            selected = currentRoute == "appointments",
            onClick = {
                if (currentRoute != "appointments") {
                    navController.navigate("appointments") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )

        // 3. Servicios
        NavigationBarItem(
            icon = { Icon(Icons.Default.Build, contentDescription = "Servicios") },
            label = { Text("Servicios") },
            selected = currentRoute == "services",
            onClick = {
                if (currentRoute != "services") {
                    navController.navigate("services") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )

        // 4. Perfil
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = currentRoute == "profile",
            onClick = {
                if (currentRoute != "profile") {
                    navController.navigate("profile") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
    }
}