package com.ketchupzz.francingsfootwear.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    @SerialName("firstname") val firstname: String,
    @SerialName("mi") val mi: String,
    @SerialName("lastname") val lastname: String,
    @SerialName("address") val address: String,
    @SerialName("country") val country: String,
    @SerialName("zipcode") val zipcode: String,
    @SerialName("mobile") val mobile: String,
    @SerialName("telephone") val telephone: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)