package com.example.serviciostec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviciostec.model.data.entities.ProductoEntity
import com.example.serviciostec.model.data.repository.ProductoRepository
import com.example.serviciostec.model.domain.CartItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(private val repository: ProductoRepository) : ViewModel() {

    val productos = repository.productos.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _carrito = MutableStateFlow<List<CartItem>>(emptyList())
    val carrito: StateFlow<List<CartItem>> = _carrito.asStateFlow()

    val totalPagar: StateFlow<Int> = _carrito.map { items ->
        items.sumOf { it.totalItem }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _estadoPago = MutableStateFlow(0)
    val estadoPago: StateFlow<Int> = _estadoPago.asStateFlow()

    init {
        viewModelScope.launch {
            repository.recargarProductosDesdeApi()
        }
    }


    fun agregarAlCarrito(item: CartItem) {
        val listaActual = _carrito.value.toMutableList()
        val existente = listaActual.find { it.producto.id == item.producto.id }

        if (existente != null) {
            existente.cantidad += item.cantidad
        } else {
            listaActual.add(item)
        }
        _carrito.value = listaActual
    }

    fun restarCantidad(item: CartItem) {
        val listaActual = _carrito.value.toMutableList()
        val existente = listaActual.find { it.producto.id == item.producto.id }

        if (existente != null) {
            if (existente.cantidad > 1) {
                existente.cantidad -= 1
            } else {
                listaActual.remove(existente)
            }
        }
        _carrito.value = listaActual
    }

    fun eliminarDelCarrito(item: CartItem) {
        val listaActual = _carrito.value.toMutableList()
        listaActual.remove(item)
        _carrito.value = listaActual
    }

    fun limpiarCarrito() {
        _carrito.value = emptyList()
    }

    fun procesarPago() {
        viewModelScope.launch {
            _estadoPago.value = 1
            delay(3000)
            limpiarCarrito()
            _estadoPago.value = 2
        }
    }

    fun resetPago() {
        _estadoPago.value = 0
    }
}