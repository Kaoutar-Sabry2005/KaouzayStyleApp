package com.example.kaouzaystyle.ui.panier

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // <-- Import important
import com.example.kaouzaystyle.R
import com.example.kaouzaystyle.data.local.entity.ProductCart

class CartAdapter(
    private var cartItems: List<ProductCart>,
    private val onQuantityChanged: (ProductCart) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.cartItemImage)
        val name: TextView = view.findViewById(R.id.cartItemName)
        val price: TextView = view.findViewById(R.id.cartItemPrice)
        val size: TextView = view.findViewById(R.id.cartItemSize)
        val colorView: View = view.findViewById(R.id.cartItemColorView)
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        val btnIncrease: TextView = view.findViewById(R.id.btnIncrease)
        val btnDecrease: TextView = view.findViewById(R.id.btnDecrease)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]

        holder.name.text = item.name
        holder.size.text = "Taille : ${item.size}"

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.image)

        holder.tvQuantity.text = item.quantity.toString()
        holder.price.text = "%.2f MAD".format(item.price * item.quantity)

        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(item.colorInt)
        shape.setStroke(2, Color.LTGRAY)
        holder.colorView.background = shape

        holder.btnIncrease.setOnClickListener {
            val newItem = item.copy(quantity = item.quantity + 1)
            onQuantityChanged(newItem)
        }

        holder.btnDecrease.setOnClickListener {
            if (item.quantity > 1) {
                val newItem = item.copy(quantity = item.quantity - 1)
                onQuantityChanged(newItem)
            }
        }
    }

    fun submitList(newList: List<ProductCart>) {
        cartItems = newList
        notifyDataSetChanged()
    }
}