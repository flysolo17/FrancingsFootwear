package com.ketchupzz.francingsfootwear.views.nav.messages

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.models.customer.Customer
import com.ketchupzz.francingsfootwear.models.messages.Messages
import com.ketchupzz.francingsfootwear.utils.timeAgoOrDateTimeFormat
import com.ketchupzz.francingsfootwear.views.adapters.MessagesAdapter

class MessageAdapter(private val context: Context,private val messages : List<Messages>,private  val customer: Customer): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SEND_MESSAGE = 0
    private val RECEIVED_MESSAGE = 1
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == SEND_MESSAGE) {
            MessageSentViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_sender, parent, false)
            )

        } else {
            MessageReceivedViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_receiver, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messages: Messages = messages[position]
        if (holder.itemViewType == SEND_MESSAGE) {
            val sentViewHolder = holder as MessageSentViewHolder
            sentViewHolder.message.text = messages.message
            sentViewHolder.timestamp.text = messages.createdAt.timeAgoOrDateTimeFormat()

        } else {
            val receivedViewHolder = holder as MessageReceivedViewHolder
            receivedViewHolder.message.text = messages.message
            receivedViewHolder.timestamp.text = messages.createdAt.timeAgoOrDateTimeFormat()
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }


    override fun getItemViewType(position: Int): Int {
        val senderID: String = messages[position].senderID
        return if (customer.id == senderID) {
            SEND_MESSAGE
        } else {
            RECEIVED_MESSAGE
        }
    }
    internal class MessageSentViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView
        var timestamp: TextView


        init {
            message = itemView.findViewById(R.id.textMessage)
            timestamp = itemView.findViewById(R.id.textDate)
        }

    }
    internal class MessageReceivedViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var message: TextView
        var timestamp: TextView
        init {
            message = itemView.findViewById(R.id.textMessage)
            timestamp = itemView.findViewById(R.id.textDate)
        }
    }
}