package com.ketchupzz.francingsfootwear.models.customer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class Contacts(
    val name : String ? = "",
    val phone : String ? = ""
) : Parcelable {
}