package com.ketchupzz.francingsfootwear.views.nav.orders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.models.transaction.PaymentMethods
import com.ketchupzz.francingsfootwear.models.transaction.PaymentStatus
import com.ketchupzz.francingsfootwear.models.transaction.TransactionStatus
import com.ketchupzz.francingsfootwear.models.transaction.Transactions
import com.ketchupzz.francingsfootwear.utils.toPHP
import com.ketchupzz.francingsfootwear.views.adapters.ItemsAdapter

interface TransactionClickListener {
    fun cancelTransaction(transactions: Transactions)
    fun viewTransaction(transactions: Transactions)
    fun payWithGCash(transactions: Transactions)
}
class TransactionAdapter(private val context: Context,private val transactions : List<Transactions>,private val transactionClickListener: TransactionClickListener) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_transactions,parent,false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.textTransactionID.text = transaction.id
        holder.textTotal.text = transaction.payment.total.toPHP()
        holder.textTransactionStatus.text = transaction.status.name
        holder.buttonCancel.setOnClickListener {
            transactionClickListener.cancelTransaction(transaction)
        }
        holder.buttonPaynow.setOnClickListener {
            transactionClickListener.payWithGCash(transaction)
        }
        holder.recyclerviewItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemsAdapter(context,transaction.items)
        }

        if (transaction.status == TransactionStatus.PENDING) {
            holder.buttonCancel.visibility = View.VISIBLE
        } else {
            holder.buttonCancel.visibility = View.GONE
        }

        if (transaction.payment.method == PaymentMethods.GCASH && transaction.payment.status == PaymentStatus.UNPAID) {
            holder.buttonPaynow.visibility = View.VISIBLE
            holder.textWarning.visibility = View.VISIBLE
        } else {
            holder.textWarning.visibility = View.GONE
        }
    }
    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textTransactionID : TextView = itemView.findViewById(R.id.textTransactionID)
        val textTransactionStatus : TextView = itemView.findViewById(R.id.textStatus)
        val textTotal : TextView = itemView.findViewById(R.id.textTotal)
        val recyclerviewItems : RecyclerView = itemView.findViewById(R.id.recyclerviewItems)
        val textWarning : TextView = itemView.findViewById(R.id.textWarning)
        val buttonCancel : TextView = itemView.findViewById(R.id.buttonCancel)
        val buttonPaynow : TextView = itemView.findViewById(R.id.buttonPaynow)
    }

}