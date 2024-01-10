package com.ketchupzz.francingsfootwear.repository.messages

import com.ketchupzz.francingsfootwear.models.messages.Messages
import com.ketchupzz.francingsfootwear.utils.UiState

interface MessagesRepository {
    fun sendMessage(messages: Messages,result : (UiState<String>) -> Unit)
    fun getAllMyMessage(uid : String , result: (UiState<List<Messages>>) -> Unit)
}