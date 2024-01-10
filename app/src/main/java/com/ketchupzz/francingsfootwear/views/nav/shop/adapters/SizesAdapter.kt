package com.ketchupzz.francingsfootwear.views.nav.shop.adapters

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.models.products.Size

class SizesAdapter(private val context: Context,private val sizes : List<Size>) : RecyclerView.Adapter<SizesAdapter.SizesViewHolder>() {

    private var selectedSize = -1;
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SizesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_sizes,parent,false)
        return SizesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
        val size = sizes[position]
        holder.textSize.text =size.size
        holder.itemView.setOnClickListener {
            selectedSize = holder.adapterPosition
            notifyDataSetChanged()
        }
        if (position == selectedSize) {
            holder.cardSize.setCardBackgroundColor(ContextCompat.getColor(context, R.color.black))
            holder.textSize.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.cardSize.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            holder.textSize.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
    }
    fun getSelectedSize()  : Int {
        return selectedSize
    }
    override fun getItemCount(): Int {
        return sizes.size
    }
    class SizesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textSize : TextView = itemView.findViewById(R.id.textSizeName)
        val cardSize : MaterialCardView = itemView.findViewById(R.id.cardSize)
    }
}