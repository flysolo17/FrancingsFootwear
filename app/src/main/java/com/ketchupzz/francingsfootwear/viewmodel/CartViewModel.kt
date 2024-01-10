package com.ketchupzz.francingsfootwear.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketchupzz.francingsfootwear.models.cart.Cart
import com.ketchupzz.francingsfootwear.repository.cart.CartRepository
import com.ketchupzz.francingsfootwear.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(val cartRepository: CartRepository): ViewModel() {
    private val _cart = MutableLiveData<UiState<List<Cart>>>()
    val cart : LiveData<UiState<List<Cart>>> get() = _cart
    fun getAllMyCart(customerID : String) {
        cartRepository.getAllMyCart(customerID) {
            _cart.value = it
        }
    }
}