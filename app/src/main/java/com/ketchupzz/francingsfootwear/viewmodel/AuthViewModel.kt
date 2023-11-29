package com.ketchupzz.francingsfootwear.viewmodel

import androidx.lifecycle.ViewModel
import com.ketchupzz.francingsfootwear.models.Customer
import com.ketchupzz.francingsfootwear.repository.AuthRepository
import com.ketchupzz.francingsfootwear.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private  val authRepository: AuthRepository) : ViewModel() {
    fun signup(customer: Customer,result : (UiState<Any>) -> Unit) {
        return authRepository.signup(customer,result)
    }
}