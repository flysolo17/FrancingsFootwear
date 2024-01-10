package com.ketchupzz.francingsfootwear.repository.cart

import com.ketchupzz.francingsfootwear.models.cart.Cart
import com.ketchupzz.francingsfootwear.utils.UiState


interface CartRepository {
    fun addToCart(cart: Cart, result : (UiState<String>) -> Unit)
    fun removeToCart(cartID : String,result: (UiState<String>) -> Unit)
    fun increaseQuantity(cartID: String,result: (UiState<String>) -> Unit)
    fun decreaseQuantity(cartID: String,result: (UiState<String>) -> Unit)
    fun getAllMyCart(customerID : String,result: (UiState<List<Cart>>) -> Unit)
}