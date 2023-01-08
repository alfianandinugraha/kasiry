package com.kasiry.app.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kasiry.app.models.data.Cart
import com.kasiry.app.models.data.Product
import com.kasiry.app.utils.datastore.localStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class CartViewModel(
    application: Application,
): AndroidViewModel(application) {
    data class StoreBody(
        val quantity: Double,
        val product: Product
    )

    private val _carts = MutableStateFlow<List<Cart>>(listOf())
    val carts = _carts.asStateFlow()

    suspend fun store(cart: StoreBody) {
        val context = getApplication<Application>().applicationContext
        val hasProduct = _carts.value.find { it.product.productId == cart.product.productId }

        lateinit var newCarts: List<Cart>

        if (hasProduct != null) {
            newCarts = _carts.value.map {
                if (it.product.productId == cart.product.productId) {
                    it.copy(quantity = it.quantity + cart.quantity)
                } else {
                    it
                }
            }
        } else {
            val newCart = Cart(
                cartId = UUID.randomUUID().toString(),
                quantity = cart.quantity,
                product = cart.product
            )
            newCarts = _carts.value + newCart
        }

        _carts.value = newCarts
        context.localStore.writeString("cart", Gson().toJson(newCarts))
    }

    suspend fun getAll(): List<Cart> {
        val context = getApplication<Application>().applicationContext
        val cartsJSON = context.localStore.readString("cart")
        val carts = Gson()
            .fromJson<List<Cart>?>(
                cartsJSON,
                object : TypeToken<List<Cart>>() {}.type
            ) ?: listOf()

        _carts.value = carts

        return carts
    }

    suspend fun delete(cartId: String) {
        val context = getApplication<Application>().applicationContext
        val newCarts = _carts.value.filter { it.cartId != cartId }
        _carts.value = newCarts
        context.localStore.writeString("cart", Gson().toJson(newCarts))
    }

    suspend fun setCart(cart: List<Cart>) {
        val context = getApplication<Application>().applicationContext
        _carts.value = cart
        context.localStore.writeString("cart", Gson().toJson(cart))
    }

    suspend fun clear() {
        val context = getApplication<Application>().applicationContext
        _carts.value = listOf()
        context.localStore.writeString("cart", Gson().toJson(_carts.value))
    }

    suspend fun setQuantity(cartId: String, quantity: Double) {
        val context = getApplication<Application>().applicationContext
        val newCarts = _carts.value.map {
            if (it.cartId == cartId) {
                it.copy(quantity = quantity)
            } else {
                it
            }
        }

        _carts.value = newCarts
        context.localStore.writeString("cart", Gson().toJson(newCarts))
    }
}