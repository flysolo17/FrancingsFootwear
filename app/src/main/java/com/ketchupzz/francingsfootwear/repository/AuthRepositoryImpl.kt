package com.ketchupzz.francingsfootwear.repository

import android.net.http.HttpException
import android.util.Log
import com.ketchupzz.francingsfootwear.models.Customer
import com.ketchupzz.francingsfootwear.services.AuthService
import com.ketchupzz.francingsfootwear.utils.UiState
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class AuthRepositoryImpl(private val authService: AuthService) : AuthRepository {
    override fun login(username: String, passwod: String, result: (UiState<Any>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun signup(customer: Customer, result: (UiState<Any>) -> Unit) {
        result.invoke(UiState.LOADING)
       authService.signUp(  customer.firstname,
           customer.mi,
           customer.lastname,
           customer.address,
           customer.country,
           customer.zipcode,
           customer.mobile,
           customer.telephone,
           customer.email,
           customer.password).enqueue(object  : Callback, retrofit2.Callback<Any> {
           override fun onResponse(call: Call<Any>, response: Response<Any>) {
               if (response.isSuccessful) {
                   result.invoke(UiState.SUCCESS("Sign up success!"))
               } else {
                   Log.d("AUTH_REPO",response.errorBody().toString())
                   result.invoke(UiState.FAILED("Unknown error"))
               }
           }

           override fun onFailure(call: Call<Any>, t: Throwable) {
               Log.d("AUTH_REPO",t.toString())
               result.invoke(UiState.FAILED(t.printStackTrace().toString()))
           }

       })
    }
}