package com.example.serviciostec.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.serviciostec.ui.components.BottomNavBar
import com.example.serviciostec.ui.components.ServiceCard
import com.example.serviciostec.ui.model.ServiceItem

@Composable
fun ServicesListScreen(navController: NavController) {
    val servicesList = listOf(
        ServiceItem("Cambio de Aceite", "$35.000", Icons.Default.Build),
        ServiceItem("Alineación y Balanceo", "$25.000", Icons.Default.Build),
        ServiceItem("Revisión de Frenos", "$15.000", Icons.Default.Build),
        ServiceItem("Cambio de Batería", "$60.000", Icons.Default.Build),
        ServiceItem("Mantención Kilometraje", "$120.000", Icons.Default.Build),
        ServiceItem("Scanner Motor", "$20.000", Icons.Default.Build)
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text("Catálogo de Servicios", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(servicesList) { service ->
                    ServiceCard(
                        service = service,
                        onAgendarClick = {
                            navController.navigate("agendar_fecha/${service.name}")
                        }
                    )
                }
            }
        }
    }
}