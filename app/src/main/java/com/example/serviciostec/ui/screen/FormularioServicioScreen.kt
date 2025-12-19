package com.example.serviciostec.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.serviciostec.ui.components.InputText
import com.example.serviciostec.viewmodel.FormularioServicioViewModel
import java.time.LocalDate

@Composable
fun FormularioServicioScreen(
    navController: NavController,
    viewModel: FormularioServicioViewModel,
    servicioInicial: String = "",
    fechaInicial: String = ""
) {
    val state by viewModel.uiState.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var patente by remember { mutableStateOf("") }

    var tipoServicio by remember { mutableStateOf(servicioInicial) }

    LaunchedEffect(state.guardadoExitoso) {
        if (state.guardadoExitoso) {
            navController.navigate("appointments") {

                popUpTo("home")
            }
            viewModel.resetEstado()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Confirmar Reserva", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (fechaInicial.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Fecha y Hora Agendada", style = MaterialTheme.typography.labelMedium)
                    Text(fechaInicial, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        InputText(nombre, { nombre = it }, "Nombre Cliente")
        Spacer(Modifier.height(8.dp))

        InputText(patente, { patente = it }, "Patente Vehículo")
        Spacer(Modifier.height(8.dp))

        InputText(
            value = tipoServicio,
            onValueChange = {
                if (servicioInicial.isEmpty()) tipoServicio = it
            },
            label = "Tipo Servicio",
            readOnly = servicioInicial.isNotEmpty()
        )

        Spacer(Modifier.height(24.dp))

        if (state.mensajeError != null) {
            Text(state.mensajeError!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val fechaFinal = if (fechaInicial.isNotBlank()) fechaInicial else LocalDate.now().toString()
                viewModel.guardarFormulario(nombre, patente, tipoServicio, fechaFinal)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("CONFIRMAR Y GUARDAR")
        }
    }
}