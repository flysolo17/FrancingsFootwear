package com.ketchupzz.francingsfootwear.models.customer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
class Addresses(
    val name : String ? = "",
    val contacts: Contacts ? = null,
    var default : Boolean ? =  null
)  : Parcelable{
}


