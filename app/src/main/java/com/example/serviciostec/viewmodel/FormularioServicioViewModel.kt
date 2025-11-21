package com.example.serviciostec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviciostec.model.data.entities.FormularioServicioEntity
import com.example.serviciostec.model.data.repository.FormularioServicioRepository
import com.example.serviciostec.model.domain.FormularioServicioUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FormularioServicioViewModel(
    private val repository: FormularioServicioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormularioServicioUIState())
    val uiState: StateFlow<FormularioServicioUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.servicios.collect { lista ->
                _uiState.update { it.copy(listaServicios = lista) }
            }
        }
    }

    fun guardarFormulario(nombre: String, patente: String, tipo: String, fecha: String) {
        if (nombre.isBlank() || patente.isBlank() || tipo.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Faltan datos obligatorios") }
            return
        }

        viewModelScope.launch {
            try {
                val nuevoServicio = FormularioServicioEntity(
                    nombreCliente = nombre,
                    patente = patente,
                    tipoServicio = tipo,
                    fecha = fecha
                )
                repository.guardarServicio(nuevoServicio)

                _uiState.update {
                    it.copy(guardadoExitoso = true, mensajeError = null)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(mensajeError = "Error al guardar: ${e.message}") }
            }
        }
    }

    fun resetEstado() {
        _uiState.update { it.copy(guardadoExitoso = false, mensajeError = null) }
    }
}