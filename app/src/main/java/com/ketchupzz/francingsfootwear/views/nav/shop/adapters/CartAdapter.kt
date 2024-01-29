package com.ketchupzz.francingsfootwear.views.nav.shop.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.models.cart.Cart
import com.ketchupzz.francingsfootwear.models.products.Product
import com.ketchupzz.francingsfootwear.models.products.Size
import com.ketchupzz.francingsfootwear.models.products.Variation
import com.ketchupzz.francingsfootwear.repository.products.PRODUCT_COLLECTION
import com.ketchupzz.francingsfootwear.repository.products.VARIATION_SUB_COLLECTION
import com.ketchupzz.francingsfootwear.utils.formatPrice


interface CartClickListener {
//    fun addQuantity(cart: Cart,size: Size,isChecked: Boolean)
//    fun decreaseQuantity(cart: Cart,size: Size,isChecked: Boolean)
    fun checkBoxIsClick(isChecked :Boolean,product: Product,variation: Variation,cart: Cart,size: Size)
}
class CartAdapter(private val context: Context,private val carts : List<Cart>,private val cartClickListener: CartClickListener) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val  view = LayoutInflater.from(context).inflate(R.layout.adapter_cart,parent,false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = carts[position]
        val firestore = FirebaseFirestore.getInstance()
        holder.getProductAndVariation(firestore,cart)
        holder.getProductAndVariation(firestore,cart)
        holder.textQuantity.text = cart.quantity.toString()
        holder.textSize.text = cart.size
        holder.checkOut.setOnClickListener {
            if (holder.size != null) {
                cartClickListener.checkBoxIsClick(holder.checkOut.isChecked,holder.product!!, variation = holder.variation!!,cart,holder.size!!)
            }

        }
    }

    fun getCartByPosition(position: Int) : Cart{
        return  carts[position]
    }
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textName : TextView = itemView.findViewById(R.id.textProductName)
        private val textVariation : TextView = itemView.findViewById(R.id.textProductVariation)
        val textSize : TextView = itemView.findViewById(R.id.textSizeName)
        val textQuantity : TextView = itemView.findViewById(R.id.textQuantity)
        val textPrice : TextView = itemView.findViewById(R.id.textPrice)
        val checkOut : CheckBox = itemView.findViewById(R.id.checkOut)
        private val imageProduct : ImageView = itemView.findViewById(R.id.imageProduct)
        var size : Size? = null
        var product : Product ?= null
        var variation : Variation ?= null
        fun getProductAndVariation(firestore: FirebaseFirestore, cart: Cart) {
            firestore.collection(PRODUCT_COLLECTION)
                .document(cart.productID)
                .get()
                .addOnSuccessListener { productDoc ->
                    if (productDoc.exists()) {
                        val product = productDoc.toObject(Product::class.java)
                        this.product = product
                        if (product != null) {
                            textName.text = product.name
                            firestore.collection(PRODUCT_COLLECTION)
                                .document(cart.productID)
                                .collection(VARIATION_SUB_COLLECTION)
                                .document(cart.variationID)
                                .get()
                                .addOnSuccessListener { variationDoc ->
                                    if (variationDoc.exists()) {
                                        val variation = variationDoc.toObject(Variation::class.java)
                                        this.variation = variation
                                        if (variation != null) {
                                            textVariation.text = variation.name
                                            size = variation.sizes.find { it.size == cart.size }
                                            val price: Double = size?.price ?: 0.00
                                            textPrice.text = formatPrice(price * cart.quantity)
                                            Glide.with(itemView.context)
                                                .load(variation.image)
                                                .error(R.drawable.product)
                                                .into(imageProduct)
                                        }
                                    }
                                }
                        }
                    }
                }
        }

    }
}