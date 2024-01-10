package com.ketchupzz.francingsfootwear.views.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ketchupzz.francingsfootwear.models.messages.Messages

class MessagesAdapter(private val context: Context,private val messages : List<Messages>) : RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return  messages.size
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
    class MessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }
}