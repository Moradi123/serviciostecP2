package com.example.serviciostec.model.domain

import com.example.serviciostec.model.data.entities.FormularioServicioEntity

data class FormularioServicioUIState(
    val listaServicios: List<FormularioServicioEntity> = emptyList(),
    val mensajeError: String? = null,
    val guardadoExitoso: Boolean = false
)