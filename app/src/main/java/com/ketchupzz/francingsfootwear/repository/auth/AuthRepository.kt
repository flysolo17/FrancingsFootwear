package com.ketchupzz.francingsfootwear.repository.auth

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.ketchupzz.francingsfootwear.models.customer.Addresses
import com.ketchupzz.francingsfootwear.models.customer.Customer
import com.ketchupzz.francingsfootwear.utils.UiState

interface AuthRepository {
    fun login(username : String , passwod : String,result : (UiState<FirebaseUser>) -> Unit)
    fun  signup(email : String , passwod: String,name : String,result: (UiState<FirebaseUser>) -> Unit)
    fun  forgotPassword(email : String ,result: (UiState<String>) -> Unit)
    fun getAccountByID(uid : String,result: (UiState<Customer>) -> Unit)

    fun reAuthenticateAccount(user: FirebaseUser, email: String, password: String, result: (UiState<FirebaseUser>) -> Unit)
    fun changePassword(user: FirebaseUser, password: String, result: (UiState<String>) -> Unit)
    fun changeProfile(uid: String, uri : Uri, imageType : String, result: (UiState<String>) -> Unit)

    fun updateFullname(uid: String  ,fullname : String,result: (UiState<String>) -> Unit)
    fun createAddress(uid: String, addresses: List<Addresses>, result: (UiState<String>) -> Unit)
}