package com.ketchupzz.francingsfootwear.views.nav.shop.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.models.products.Variation

interface VariationClickListener {
    fun onVariationClicked(variation: Variation)
}
class VariationAdapter(private val context : Context ,private val variations : List<Variation>,private val variationClickListener: VariationClickListener) : RecyclerView.Adapter<VariationAdapter.VariationViewHolder>() {
    private var selectedVariation = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_variations,parent,false)
        return VariationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return variations.size
    }
    fun getSelectedVariation() : Int {
        return selectedVariation;
    }
    override fun onBindViewHolder(holder: VariationViewHolder, position: Int) {
        val variation = variations[position]
        Glide.with(context)
            .load(variation.image)
            .error(R.drawable.product)
            .into(holder.imageVariation)
        holder.textVariation.text = variation.name
        holder.itemView.setOnClickListener {
            selectedVariation = holder.adapterPosition
            notifyDataSetChanged()
            variationClickListener.onVariationClicked(variation)
        }
        if (position == selectedVariation) {
            holder.imageVariation.setBackgroundResource(R.drawable.selected_variation_border)
        } else {
            holder.imageVariation.setBackgroundResource(0)
        }
    }
    class VariationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageVariation : ImageView = itemView.findViewById(R.id.imageVariation)
        val textVariation : TextView = itemView.findViewById(R.id.textVariationName)
    }
}