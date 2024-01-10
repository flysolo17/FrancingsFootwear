package com.ketchupzz.francingsfootwear.models.transaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Payment(
    val total : Double = 0.00,
    val method : PaymentMethods = PaymentMethods.COD,
    var status : PaymentStatus = PaymentStatus.UNPAID,
    var receipt : String = "",
    val paymentDate : Date ? = null
) : Parcelable {
}

enum class PaymentMethods {
    COD,
    GCASH
}

enum class PaymentStatus {
    PAID,
    UNPAID
}
