package com.example.serviciostec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviciostec.model.data.dao.VehiculoDao
import com.example.serviciostec.model.data.entities.VehiculoEntity
import kotlinx.coroutines.launch

class VehiculoViewModel(private val dao: VehiculoDao) : ViewModel() {

    fun obtenerVehiculos(usuarioId: String) = dao.obtenerVehiculosPorUsuario(usuarioId)

    fun agregarVehiculo(marca: String, modelo: String, anio: String, patente: String, usuarioId: String) {
        viewModelScope.launch {
            val nuevoVehiculo = VehiculoEntity(
                marca = marca,
                modelo = modelo,
                anio = anio,
                patente = patente,
                usuarioId = usuarioId
            )
            dao.insertarVehiculo(nuevoVehiculo)
        }
    }

    fun eliminarVehiculo(vehiculo: VehiculoEntity) {
        viewModelScope.launch {
            dao.eliminarVehiculo(vehiculo)
        }
    }
}