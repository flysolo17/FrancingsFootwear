package com.ketchupzz.francingsfootwear.views.nav.shop.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.models.transaction.Payment
import com.ketchupzz.francingsfootwear.models.transaction.PaymentMethods
class PaymentAdapter(private val context : Context,private val methods : List<PaymentMethods>): RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {
    private var selectedPayment = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view  = LayoutInflater.from(context).inflate(R.layout.list_payment,parent,false)
        return PaymentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  methods.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = methods[position]
        if (payment == PaymentMethods.COD) {
            with(holder) {
                textPaymentType.text = "Cash On Delivery"
                textPaymentTypeDesc.text = "Pay when you receive"
                imagePaymentType.setImageResource(R.drawable.cod)
            }
        } else {
            with(holder) {
                textPaymentType.text = "GCASH Payment"
                textPaymentTypeDesc.text = "Pay with e-wallet"
                imagePaymentType.setImageResource(R.drawable.gcash)
            }
        }
        holder.itemView.setOnClickListener {
            selectedPayment = holder.adapterPosition
            notifyDataSetChanged()
        }
        if (position == selectedPayment) {
            holder.cardPaymentType.setCardBackgroundColor(ContextCompat.getColor(context, R.color.accent2))
        } else {
            holder.cardPaymentType.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

    }
    fun getSelected() : Int {
        return selectedPayment;
    }
    class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textPaymentType : TextView = itemView.findViewById(R.id.textPaymentType)
        val textPaymentTypeDesc : TextView = itemView.findViewById(R.id.textPaymentDesc)
        val imagePaymentType : ImageView = itemView.findViewById(R.id.imagePaymentType)
        val  cardPaymentType : MaterialCardView = itemView.findViewById(R.id.cardPayment)

    }
}