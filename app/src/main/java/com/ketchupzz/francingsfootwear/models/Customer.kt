package com.ketchupzz.francingsfootwear.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id : String ? = null,
    val name : String ? = null,
    val profile : String ? = null,
    val email : String ? = null,
    val addresses : List<String> = listOf()
)