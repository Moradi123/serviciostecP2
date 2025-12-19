package com.example.serviciostec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviciostec.model.data.dao.VehiculoDao
import com.example.serviciostec.model.data.entities.VehiculoEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VehiculoViewModel(private val dao: VehiculoDao) : ViewModel() {

    val vehiculos: StateFlow<List<VehiculoEntity>> = dao.getAllVehiculos()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    fun agregarVehiculo(vehiculo: VehiculoEntity) {
        viewModelScope.launch { dao.insertVehiculo(vehiculo) }
    }
    fun eliminarVehiculo(vehiculo: VehiculoEntity) {
        viewModelScope.launch { dao.deleteVehiculo(vehiculo) }
    }
}