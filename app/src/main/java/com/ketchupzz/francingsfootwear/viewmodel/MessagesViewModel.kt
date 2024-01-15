package com.ketchupzz.francingsfootwear.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketchupzz.francingsfootwear.models.messages.Messages
import com.ketchupzz.francingsfootwear.repository.messages.MessagesRepository
import com.ketchupzz.francingsfootwear.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor( val messagesRepository: MessagesRepository): ViewModel() {
    private val _messages = MutableLiveData<UiState<List<Messages>>>()
    val messages : LiveData<UiState<List<Messages>>> get() = _messages


    fun getAllMessages(uid : String) {
        messagesRepository.getAllMyMessage(uid) {
            _messages.value = it
        }
    }

}