package com.ketchupzz.francingsfootwear.repository

import com.google.firebase.auth.FirebaseUser
import com.ketchupzz.francingsfootwear.models.Customer
import com.ketchupzz.francingsfootwear.utils.UiState

interface AuthRepository {
    fun login(username : String , passwod : String,result : (UiState<FirebaseUser>) -> Unit)

    fun  signup(email : String , passwod: String,name : String,result: (UiState<FirebaseUser>) -> Unit)
}