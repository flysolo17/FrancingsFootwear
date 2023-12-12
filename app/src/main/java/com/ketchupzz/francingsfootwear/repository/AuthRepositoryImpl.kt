package com.ketchupzz.francingsfootwear.repository


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzz.francingsfootwear.models.Customer

import com.ketchupzz.francingsfootwear.utils.UiState

const val USER_COLLECTION = "users";
class AuthRepositoryImpl(private  val firestore : FirebaseFirestore,private  val auth : FirebaseAuth) : AuthRepository {
    override fun login(email: String, passwod: String, result: (UiState<FirebaseUser>) -> Unit) {
        result.invoke(UiState.LOADING)
        auth.signInWithEmailAndPassword(email,passwod)
            .addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user
                if (user != null) {
                    result.invoke(UiState.SUCCESS(user))
                } else {
                    result.invoke(UiState.FAILED("User not Found!"))
                }

            } else {
                result.invoke(UiState.FAILED("Failed to logged in.."))
            }
        }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
        }
    }

    override fun signup(email : String , passwod: String,name : String, result: (UiState<FirebaseUser>) -> Unit) {
        result.invoke(UiState.LOADING)
        auth.createUserWithEmailAndPassword(email,passwod).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user
                if (user != null) {
                    val  customer = Customer(
                        id = user.uid,
                        name = name,
                        profile =   "",
                        email = email,
                        addresses =  emptyList()
                    )
                    firestore.collection(USER_COLLECTION).document(user.uid).set(customer).addOnCompleteListener {
                        if (it.isSuccessful) {
                            result.invoke(UiState.SUCCESS(user))
                        } else {
                            result.invoke(UiState.FAILED("Error Saving account"))
                        }
                    }.addOnFailureListener {
                        result.invoke(UiState.FAILED(it.message.toString()))
                    }

                } else {
                    result.invoke(UiState.FAILED("Error Creating user.."))
                }
            } else {
                result.invoke(UiState.FAILED("Error Creating user.."))
            }
        }.addOnFailureListener {
            result.invoke(UiState.FAILED(it.message.toString()))
        }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("We sent password reset link to : ${email}"))

                } else {
                    result.invoke(UiState.FAILED("Failed to logged in.."))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }
}