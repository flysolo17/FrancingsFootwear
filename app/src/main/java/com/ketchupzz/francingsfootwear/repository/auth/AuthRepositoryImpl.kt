package com.ketchupzz.francingsfootwear.repository.auth


import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.ketchupzz.francingsfootwear.models.customer.Addresses
import com.ketchupzz.francingsfootwear.models.customer.Customer

import com.ketchupzz.francingsfootwear.utils.UiState
import java.util.UUID

const val USER_COLLECTION = "users";
class AuthRepositoryImpl(private  val firestore : FirebaseFirestore,private  val auth : FirebaseAuth,private val storage: FirebaseStorage) :
    AuthRepository {
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
        auth.createUserWithEmailAndPassword(email,passwod).addOnCompleteListener { it ->
            if (it.isSuccessful) {
                val user = it.result.user
                if (user != null) {
                    val  customer = Customer(
                        id = user.uid,
                        name = name,
                        profile =   "",
                        email = email,
                        addresses =  mutableListOf()
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
        result.invoke(UiState.LOADING)
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

    override fun getAccountByID(uid: String, result: (UiState<Customer>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(USER_COLLECTION)
            .document(uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    result.invoke(UiState.FAILED(error.message.toString()))
                }
                if (value != null && value.exists()) {
                    val customer = value.toObject(Customer::class.java)
                    if (customer != null) {
                        result.invoke(UiState.SUCCESS(customer))
                    } else {
                        result.invoke(UiState.FAILED("User does not exist"))
                    }
                } else {
                    result.invoke(UiState.FAILED("User does not exist"))
                }
            }
    }
    override fun reAuthenticateAccount(
        user : FirebaseUser,
        email: String,
        password: String,
        result: (UiState<FirebaseUser>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        val credential = EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS(user))
                } else  {
                    result.invoke(UiState.FAILED("Wrong Password!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }


    override fun changePassword(
        user: FirebaseUser,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        user.updatePassword(password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Password changed successfully"))
                } else  {
                    result.invoke(UiState.FAILED("Wrong Password!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message!!))
            }
    }

    override fun changeProfile(
        uid: String,
        uri: Uri,
        imageType: String,
        result: (UiState<String>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        val uniqueImageName = UUID.randomUUID().toString()
        val imagesRef: StorageReference = storage.reference.child("students/${uid}/$uniqueImageName.jpg")
        val uploadTask: UploadTask = imagesRef.putFile(uri)

        uploadTask.addOnCompleteListener { taskSnapshot ->
            if (taskSnapshot.isSuccessful) {
                imagesRef.downloadUrl.addOnCompleteListener { urlTask: Task<Uri> ->
                    if (urlTask.isSuccessful) {
                        val imageUrl: String = urlTask.result.toString()
                        val userDocRef = firestore.collection(USER_COLLECTION).document(uid)
                        userDocRef.update("profile", imageUrl)
                            .addOnSuccessListener {
                                result(UiState.SUCCESS("Profile image updated successfully."))
                            }
                            .addOnFailureListener { e ->
                                result(UiState.FAILED(e.message.toString()))
                            }
                    } else {
                        result(UiState.FAILED("Failed to get image URL."))
                    }
                }
            } else {
                result(UiState.FAILED("Failed to upload image."))
            }
        }
    }

    override fun updateFullname(uid: String, fullname: String, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(USER_COLLECTION)
            .document(uid)
            .update("name",fullname)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully Updated!"))
                } else {
                    result.invoke(UiState.FAILED("unknown error!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    override fun createAddress(uid: String, addresses: List<Addresses>, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(USER_COLLECTION)
            .document(uid)
            .update("addresses",addresses)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Successfully Added!"))
                } else {
                    result.invoke(UiState.FAILED("Failed creating address!"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.FAILED(it.message.toString()))
            }

    }


}