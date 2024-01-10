package com.ketchupzz.francingsfootwear.repository.transaction

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.ketchupzz.francingsfootwear.models.transaction.Payment
import com.ketchupzz.francingsfootwear.models.transaction.PaymentStatus
import com.ketchupzz.francingsfootwear.models.transaction.TransactionStatus
import com.ketchupzz.francingsfootwear.models.transaction.Transactions
import com.ketchupzz.francingsfootwear.utils.UiState
import java.util.UUID

const val TRANSACTION_COLLECTION = "transactions";
class TransactionRepositoryImpl(private val firestore: FirebaseFirestore,private val storage: FirebaseStorage) : TransactionRepository {
    override fun createTransaction(transactions: Transactions, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(TRANSACTION_COLLECTION)
            .document(transactions.id)
            .set(transactions)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Order successful"))
                } else {
                    result.invoke(UiState.FAILED("Order failed"))
                }
            }.addOnFailureListener {
                Log.d(TRANSACTION_COLLECTION,it.message.toString())
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }

    override fun getAllTransactionByCustomerID(
        customerID: String,
        result: (UiState<List<Transactions>>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        firestore.collection(TRANSACTION_COLLECTION)
            .whereEqualTo("customerID",customerID)
            .orderBy("orderDate",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.d(TRANSACTION_COLLECTION,it.message.toString())
                    result.invoke(UiState.FAILED(it.message.toString()))
                }
                value?.let {
                    Log.d(TRANSACTION_COLLECTION,it.toObjects(Transactions::class.java).size.toString())
                    result.invoke(UiState.SUCCESS(it.toObjects(Transactions::class.java)))
                }
            }
    }

    override fun uploadReceipt(transactionID: String,uri: Uri,payment: Payment,type : String, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        val uniqueImageName = UUID.randomUUID().toString()
        val imagesRef: StorageReference = storage.reference.child("$TRANSACTION_COLLECTION/${transactionID}/$uniqueImageName.${type}")
        val uploadTask: UploadTask = imagesRef.putFile(uri)
        uploadTask.addOnCompleteListener { taskSnapshot ->
            if (taskSnapshot.isSuccessful) {
                imagesRef.downloadUrl.addOnCompleteListener { urlTask: Task<Uri> ->
                    if (urlTask.isSuccessful) {
                        val imageUrl: String = urlTask.result.toString()
                        payment.receipt = imageUrl
                        payment.status = PaymentStatus.PAID
                        val userDocRef = firestore.collection(TRANSACTION_COLLECTION).document(transactionID)
                        userDocRef.update("payment", payment)
                            .addOnSuccessListener {
                                result(UiState.SUCCESS("Payment updated successfully."))
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

    override fun cancelTransaction(transactionID: String, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(TRANSACTION_COLLECTION)
            .document(transactionID)
            .update("status",TransactionStatus.CANCELLED.name)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("Order cancelled"))
                } else {
                    result.invoke(UiState.FAILED("Order failed to cancel"))
                }
            }.addOnFailureListener {
                Log.d(TRANSACTION_COLLECTION,it.message.toString())
                result.invoke(UiState.FAILED(it.message.toString()))
            }
    }
}