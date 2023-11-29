package com.ketchupzz.francingsfootwear.repository

import com.ketchupzz.francingsfootwear.models.Customer
import com.ketchupzz.francingsfootwear.utils.UiState

interface AuthRepository {
    fun login(username : String , passwod : String,result : (UiState<Any>) -> Unit)

    fun  signup(customer: Customer,result: (UiState<Any>) -> Unit)
}