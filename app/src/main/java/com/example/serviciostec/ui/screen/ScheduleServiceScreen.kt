package com.example.serviciostec.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleServiceScreen(
    navController: NavController,
    serviceName: String
) {
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    var showTimePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar: $serviceName") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("1. Selecciona la Fecha", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            DatePicker(
                state = datePickerState,
                modifier = Modifier.weight(1f),
                showModeToggle = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("2. Selecciona la Hora", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            TimeInput(state = timePickerState)
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    if (selectedDateMillis != null) {
                        val date = Instant.ofEpochMilli(selectedDateMillis)
                            .atZone(ZoneId.of("UTC")) // <--- CAMBIO IMPORTANTE
                            .toLocalDate()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                        val time = "${timePickerState.hour}:${String.format("%02d", timePickerState.minute)}"
                        val fechaCompleta = "$date $time"

                        navController.navigate("confirmar_reserva/$serviceName/$fechaCompleta")
                    }
                },
                enabled = datePickerState.selectedDateMillis != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CONFIRMAR FECHA Y HORA")
            }
        }
    }
}