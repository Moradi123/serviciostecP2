package com.example.serviciostec.model.domain

import com.example.serviciostec.model.data.entities.ProductoEntity

data class CartItem(
    val producto: ProductoEntity,
    var cantidad: Int = 1
) {
    val totalItem: Int
        get() = producto.precio * cantidad
}