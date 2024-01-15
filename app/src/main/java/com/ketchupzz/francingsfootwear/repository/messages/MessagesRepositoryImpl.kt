package com.ketchupzz.francingsfootwear.repository.messages

import android.util.Log
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ketchupzz.francingsfootwear.models.messages.Messages
import com.ketchupzz.francingsfootwear.utils.UiState
const val MESSAGES_COLLECTION = "messages"
class MessagesRepositoryImpl(private val firestore: FirebaseFirestore): MessagesRepository {
    override fun sendMessage(messages: Messages, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(MESSAGES_COLLECTION)
            .add(messages)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.SUCCESS("message sent!"))
                } else {
                    result.invoke(UiState.SUCCESS("message failed to sent!"))
                }
            }
    }

    override fun getAllMyMessage(uid : String,result: (UiState<List<Messages>>) -> Unit) {
        result.invoke(UiState.LOADING)
        firestore.collection(MESSAGES_COLLECTION)
            .where(Filter.or(
                Filter.equalTo("senderID",uid),
                Filter.equalTo("receiverID",uid))
            ).orderBy(
                "createdAt",
                Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    result.invoke(UiState.FAILED(it.message.toString()))
                    Log.d("messages",it.message.toString())
                }
                value?.let {
                    result.invoke(UiState.SUCCESS(it.toObjects(Messages::class.java)))
                }
            }
    }
}