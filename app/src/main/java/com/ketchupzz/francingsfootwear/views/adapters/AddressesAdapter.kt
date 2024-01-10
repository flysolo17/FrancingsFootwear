package com.ketchupzz.francingsfootwear.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.models.customer.Addresses

interface  OnAddressAdapterClicked {
    fun updateDefaultAddress(position: Int)
}
class AddressesAdapter(private val context : Context, private val addressesList : List<Addresses>,private val onAddressAdapterClicked: OnAddressAdapterClicked): RecyclerView.Adapter<AddressesAdapter.AddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_addresses,parent,false)
        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  addressesList.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val addresses = addressesList[position]
        holder.textAddress.text = addresses.name
        holder.textName.text = addresses.contacts?.name ?: "no name"
        holder.phone.text = addresses.contacts?.phone ?: "no phone"
        if (addresses.default!!) {
            holder.defaultAddress.visibility = View.VISIBLE
        } else {
            holder.defaultAddress.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            onAddressAdapterClicked.updateDefaultAddress(position)
        }
    }
    class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textAddress: TextView = itemView.findViewById(R.id.textAddress)
        val phone: TextView = itemView.findViewById(R.id.textPhone)
        val textName : TextView = itemView.findViewById(R.id.textFullname)
        val defaultAddress : TextView = itemView.findViewById(R.id.textDefaultAddress)
    }
}