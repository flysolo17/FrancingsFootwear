package com.ketchupzz.francingsfootwear.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.ketchupzz.francingsfootwear.models.customer.Addresses
import com.ketchupzz.francingsfootwear.models.customer.Customer
import com.ketchupzz.francingsfootwear.repository.auth.AuthRepository
import com.ketchupzz.francingsfootwear.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private  val authRepository: AuthRepository) : ViewModel() {

    private val _currentUser= MutableLiveData<FirebaseUser>()
    val currentUser : LiveData<FirebaseUser> get() = _currentUser


    private val _customer= MutableLiveData<UiState<Customer>>()
    val customer : LiveData<UiState<Customer>> get() = _customer
    fun setCurrentUser(user : FirebaseUser) {
        _currentUser.value = user
    }
    fun signup(email : String,pasword : String,name : String,result : (UiState<FirebaseUser>) -> Unit) {
        return authRepository.signup(email,pasword,name,result)
    }
    fun login(email: String,pasword: String,result: (UiState<FirebaseUser>) -> Unit) {
        return authRepository.login(email,pasword,result)
    }
    fun forgotPassword(email: String,result: (UiState<String>) -> Unit) {
        return authRepository.forgotPassword(email,result)
    }
    fun getCustomerInfo(uid : String) {
         authRepository.getAccountByID(uid) {
             _customer.value = it
         }
    }
    fun changeProfile(uid: String,
                      uri: Uri,
                      imageType: String,
                      result: (UiState<String>) -> Unit) {
        return authRepository.changeProfile(uid, uri, imageType, result)
    }

    fun editProfile(uid: String,fullname : String,result: (UiState<String>) -> Unit) {
        return authRepository.updateFullname(uid, fullname, result)
    }

    fun changePassword(user: FirebaseUser,password: String,result: (UiState<String>) -> Unit) {
        return authRepository.changePassword(user,password,result)
    }

    fun reauthenticate(user: FirebaseUser,email: String,password: String,result: (UiState<FirebaseUser>) -> Unit) {
        return authRepository.reAuthenticateAccount(user,email,password,result)
    }

    fun createAddress(uid: String, addresses: List<Addresses>, result: (UiState<String>) -> Unit) {
        return authRepository.createAddress(uid, addresses, result)
    }
}