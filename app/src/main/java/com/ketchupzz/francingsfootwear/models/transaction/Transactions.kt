package com.ketchupzz.francingsfootwear.models.transaction

import android.os.Parcelable
import com.ketchupzz.francingsfootwear.models.customer.Addresses
import com.ketchupzz.francingsfootwear.utils.generateRandomNumber
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.Date


@Parcelize
data class Transactions(
    val id : String  = generateRandomNumber(20),
    val customerID : String = "",
    val items : List<Items> = mutableListOf(),
    val status: TransactionStatus = TransactionStatus.PENDING,
    val payment: Payment = Payment(),
    val shippingFee : Double = 100.00,
    val address: Addresses = Addresses(),
    val details: List<Details> = mutableListOf(),
    val orderDate : Date = Date()
) : Parcelable {}


enum class TransactionStatus {
    PENDING,
    ACCEPTED,
    ON_DELIVERY,
    COMPLETE,
    CANCELLED,
    DECLINED
}

