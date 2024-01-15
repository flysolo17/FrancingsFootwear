package com.ketchupzz.francingsfootwear.views.nav.shop.adapters

import android.adservices.adid.AdId
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.ketchupzz.francingsfootwear.R
import com.ketchupzz.francingsfootwear.models.products.Product
import com.ketchupzz.francingsfootwear.models.products.Variation
import com.ketchupzz.francingsfootwear.repository.products.PRODUCT_COLLECTION
import com.ketchupzz.francingsfootwear.repository.products.VARIATION_SUB_COLLECTION
import com.ketchupzz.francingsfootwear.utils.UiState
import com.ketchupzz.francingsfootwear.utils.getEffectiveProductPrice
import com.ketchupzz.francingsfootwear.viewmodel.ProductViewModel

interface ProductAdapterClickListener {
    fun onClick(product: Product,variations : List<Variation>)
}
class ProductAdapter(private val context: Context, private var products : List<Product>, private  val productAdapterClickListener: ProductAdapterClickListener) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    private val originalList: List<Product> = products
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_product,parent,false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val firestore = FirebaseFirestore.getInstance()
        holder.textProductName.text = product.name
        Glide.with(context)
            .load(product.image)
            .error(R.drawable.product)
            .into(holder.imageProduct)

        holder.itemView.setOnClickListener {
            productAdapterClickListener.onClick(product,holder.variations)
        }
       holder.getProductPrice(firestore,product.id ?: "")
    }

    fun filter(query: String) {
        products = if (query.isBlank()) {
            // If the query is empty, reset to the original list
            originalList
        } else {
            // Filter the list based on the search query
            originalList.filter { it.name!!.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textProductName : TextView = itemView.findViewById(R.id.textProductName)
        val imageProduct : ImageView = itemView.findViewById(R.id.imageProduct)
        val textPrice : TextView = itemView.findViewById(R.id.textPrice)
        val variations : MutableList<Variation> = mutableListOf()
        fun getProductPrice(firestore: FirebaseFirestore,productID: String) {
            variations.clear()
            firestore.collection(PRODUCT_COLLECTION)
                .document(productID)
                .collection(VARIATION_SUB_COLLECTION)
                .get()
                .addOnSuccessListener {
                    val variations = it.toObjects(Variation::class.java)
                    this.variations.addAll(variations)
                    textPrice.text = getEffectiveProductPrice(variations)
                }
        }
    }
}