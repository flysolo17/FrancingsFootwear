package com.ketchupzz.francingsfootwear.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.ketchupzz.francingsfootwear.models.Customer
import com.ketchupzz.francingsfootwear.repository.AuthRepository
import com.ketchupzz.francingsfootwear.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private  val authRepository: AuthRepository) : ViewModel() {
    fun signup(email : String,pasword : String,name : String,result : (UiState<FirebaseUser>) -> Unit) {
        return authRepository.signup(email,pasword,name,result)
    }
    fun login(email: String,pasword: String,result: (UiState<FirebaseUser>) -> Unit) {
        return authRepository.login(email,pasword,result)
    }
    fun forgotPassword(email: String,result: (UiState<String>) -> Unit) {
        return authRepository.forgotPassword(email,result)
    }
}