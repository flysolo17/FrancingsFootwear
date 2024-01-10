package com.ketchupzz.francingsfootwear.views.nav.shop.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.ketchupzz.francingsfootwear.R


interface OnItemClickListener {
    fun onClick(imageView: ImageView?, path: String?)
}
class ImageAdapter(private val context: Context,private  val images : List<Int>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageview : ImageView = itemView.findViewById(R.id.list_item_image)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageAdapter.ImageViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.image_list_item,parent,false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageAdapter.ImageViewHolder, position: Int) {
        holder.imageview.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

}