package com.ketchupzz.francingsfootwear.repository.transaction

import android.net.Uri
import com.ketchupzz.francingsfootwear.models.transaction.Payment
import com.ketchupzz.francingsfootwear.models.transaction.Transactions
import com.ketchupzz.francingsfootwear.utils.UiState

interface TransactionRepository {
    fun createTransaction(transactions: Transactions ,result : (UiState<String>) -> Unit)
    fun getAllTransactionByCustomerID(customerID : String ,result: (UiState<List<Transactions>>) -> Unit)
    fun uploadReceipt(transactionID: String,uri: Uri,payment : Payment,type : String,result: (UiState<String>) -> Unit)
    fun cancelTransaction(transactionID : String,result: (UiState<String>) -> Unit)
}