package com.ketchupzz.francingsfootwear.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.ketchupzz.francingsfootwear.models.messages.Messages
import com.ketchupzz.francingsfootwear.models.products.Variation
import com.ketchupzz.francingsfootwear.models.transaction.Items
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

fun Context.getImageTypeFromUri(imageUri: Uri?): String? {
    val contentResolver: ContentResolver = contentResolver
    val mimeTypeMap = MimeTypeMap.getSingleton()
    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri!!))
}

fun getEffectiveProductPrice(variations: List<Variation>): String {
    val minPrices = variations.flatMap { it.sizes.map { size -> size.price } }.minOrNull() ?: 0.00
    val maxPrices = variations.flatMap { it.sizes.map { size -> size.price } }.maxOrNull() ?: 0.00
    return if (minPrices == maxPrices) {
        "₱ ${minPrices.format()}"

    } else {
        "₱ ${minPrices.format()} - ₱ ${maxPrices.format()}"
    }
}

fun Double.format(): String {
    return String.format("%.2f", this)
}
fun formatPrice(num : Double) : String {
    return "₱ ${num.format()}"
}

fun generateRandomNumber(repeat : Int): String {
    // Generate a random 10-digit number
    val randomNumber = buildString {
        repeat(repeat) {
            append(Random.nextInt(0,10))
        }
    }
    return randomNumber
}
fun computeItemSubtotal(items : List<Items>) : Double {
    var subtotal  = 0.00;
    items.map {
        subtotal += (it.quantity * it.price)
    }
    return subtotal
}

fun countItems(items: List<Items>) : Int {
    var count = 0
    items.map {
        count += it.quantity
    }
    return count;
}
fun Double.toPHP(): String {
    val formattedString = String.format("₱ %,.2f", this)
    return formattedString
}


fun calculateShippingFee(numberOfItems: Int): Double {
    return when (numberOfItems) {
        in 1..10 -> 100.00
        in 11..20 -> 200.00
        in 21..30 -> 300.00
        in 31..40 -> 400.00
        in 41..50 -> 500.00
        in 51..60 -> 600.00
        in 61..70 -> 700.00
        in 71..80 -> 800.00
        in 81..90-> 900.00
        else -> 1000.00
    }
}

fun Date.timeAgoOrDateTimeFormat(): String {
    val now = Date()
    val timeDifference = now.time - this.time
    val minutesInMillis = 60 * 1000
    val hoursInMillis = 60 * minutesInMillis
    val daysInMillis = 24 * hoursInMillis

    return when {
        timeDifference < minutesInMillis -> "just now"
        timeDifference < hoursInMillis -> "${timeDifference / minutesInMillis} minute${if (timeDifference / minutesInMillis > 1) "s" else ""} ago"
        timeDifference < daysInMillis -> "${timeDifference / hoursInMillis} hour${if (timeDifference / hoursInMillis > 1) "s" else ""} ago"
        else -> SimpleDateFormat("MMM/dd/yyyy HH:mm", Locale.getDefault()).format(this)
    }
}

fun List<Messages>.getLastMessage(myID : String) : Int {
    var count = 0
    for (message in this) {
        if (message.senderID == myID) {
            break
        }
        count += 1
    }
    return count
}
