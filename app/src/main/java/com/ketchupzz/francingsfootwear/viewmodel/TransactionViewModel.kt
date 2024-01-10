package com.ketchupzz.francingsfootwear.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketchupzz.francingsfootwear.models.transaction.Transactions
import com.ketchupzz.francingsfootwear.repository.transaction.TransactionRepository
import com.ketchupzz.francingsfootwear.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(val transactionRepository: TransactionRepository): ViewModel() {
    private val _transactions = MutableLiveData<UiState<List<Transactions>>>()
    val transactions :LiveData<UiState<List<Transactions>>> get() = _transactions

    fun getAllMyTransactions(customerID : String) {
        transactionRepository.getAllTransactionByCustomerID(customerID){
            _transactions.value = it
        }
    }
}