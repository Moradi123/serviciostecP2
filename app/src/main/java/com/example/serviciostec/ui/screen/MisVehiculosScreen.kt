package com.example.serviciostec.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.serviciostec.ui.components.BottomNavBar
import com.example.serviciostec.ui.components.InputText
import com.example.serviciostec.viewmodel.UserViewModel
import com.example.serviciostec.viewmodel.VehiculoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisVehiculosScreen(
    navController: NavController,
    vehiculoViewModel: VehiculoViewModel,
    userViewModel: UserViewModel
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val userId = currentUser?.usuario ?: ""

    val vehiculos by vehiculoViewModel.obtenerVehiculos(userId).collectAsState(initial = emptyList())

    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Garage") })
        },
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogo = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Auto")
            }
        }
    ) { padding ->
        if (vehiculos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No tienes vehículos registrados", color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
                items(vehiculos) { auto ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.Blue, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "${auto.marca} ${auto.modelo}", style = MaterialTheme.typography.titleMedium)
                                Text(text = "Patente: ${auto.patente} - Año: ${auto.anio}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            }
                            IconButton(onClick = { vehiculoViewModel.eliminarVehiculo(auto) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }

        if (mostrarDialogo) {
            AgregarVehiculoDialog(
                onDismiss = { mostrarDialogo = false },
                onAdd = { marca, modelo, anio, patente ->
                    vehiculoViewModel.agregarVehiculo(marca, modelo, anio, patente, userId)
                    mostrarDialogo = false
                }
            )
        }
    }
}

@Composable
fun AgregarVehiculoDialog(onDismiss: () -> Unit, onAdd: (String, String, String, String) -> Unit) {
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var patente by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Vehículo") },
        text = {
            Column {
                InputText(marca, { marca = it }, "Marca (ej: Toyota)")
                Spacer(Modifier.height(8.dp))
                InputText(modelo, { modelo = it }, "Modelo (ej: Yaris)")
                Spacer(Modifier.height(8.dp))
                InputText(anio, { anio = it }, "Año")
                Spacer(Modifier.height(8.dp))
                InputText(patente, { patente = it }, "Patente")
            }
        },
        confirmButton = {
            Button(onClick = {
                if (marca.isNotEmpty() && patente.isNotEmpty()) {
                    onAdd(marca, modelo, anio, patente)
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}