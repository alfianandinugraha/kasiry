package com.kasiry.app.models.data

import android.net.Uri

data class Cart(
    val cartId: String,
    val quantity: Double,
    val product: Product
) {
    override fun toString(): String {
        return "Cart(cartId='$cartId', quantity=$quantity, product=$product)"
    }
}
