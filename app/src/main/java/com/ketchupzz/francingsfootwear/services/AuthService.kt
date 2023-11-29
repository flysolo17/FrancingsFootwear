package com.ketchupzz.francingsfootwear.services

import com.ketchupzz.francingsfootwear.models.Customer
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST



interface AuthService {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<Any>

    @FormUrlEncoded
    @POST("function/customer_signup.php")
    fun signUp(
        @Field("firstname") firstname: String,
        @Field("mi") mi: String,
        @Field("lastname") lastname: String,
        @Field("address") address: String,
        @Field("country") country: String,
        @Field("zipcode") zipcode: String,
        @Field("mobile") mobile: String,
        @Field("telephone") telephone: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Any>
}