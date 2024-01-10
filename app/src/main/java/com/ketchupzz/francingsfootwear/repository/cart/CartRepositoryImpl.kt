package com.ketchupzz.francingsfootwear.repository.cart

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ketchupzz.francingsfootwear.models.cart.Cart
import com.ketchupzz.francingsfootwear.utils.UiState

const val CART_COLLECTION = "carts"
class CartRepositoryImpl(private  val firestore: FirebaseFirestore) : CartRepository {
    override fun addToCart(cart: Cart, result: (UiState<String>) -> Unit) {
        firestore.collection(CART_COLLECTION)
            .document(cart.id)
            .set(cart)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully Added!"))
                } else {
                    result.invoke(UiState.SUCCESS("Failed to add in cart"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    override fun removeToCart(cartID: String, result: (UiState<String>) -> Unit) {
        firestore.collection(CART_COLLECTION)
            .document(cartID)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully deleted!"))
                } else {
                    result.invoke(UiState.SUCCESS("Failed to remove in cart"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    override fun increaseQuantity(cartID: String, result: (UiState<String>) -> Unit) {
        firestore.collection(CART_COLLECTION)
            .document(cartID)
            .update("quantity",FieldValue.increment(1))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully updated!"))
                } else {
                    result.invoke(UiState.FAILED("Failed to update in cart"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    override fun decreaseQuantity(cartID: String, result: (UiState<String>) -> Unit) {
        firestore.collection(CART_COLLECTION)
            .document(cartID)
            .update("quantity",FieldValue.increment(-1))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully updated!"))
                } else {
                    result.invoke(UiState.FAILED("Failed to update in cart"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    override fun getAllMyCart(customerID: String, result: (UiState<List<Cart>>) -> Unit) {
        firestore.collection(CART_COLLECTION)
            .whereEqualTo("customerID",customerID)
            .orderBy("addedAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.d("cart",it.message.toString())
                    result.invoke(UiState.FAILED(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.SUCCESS(it.toObjects(Cart::class.java)))
                }
            }
    }

}