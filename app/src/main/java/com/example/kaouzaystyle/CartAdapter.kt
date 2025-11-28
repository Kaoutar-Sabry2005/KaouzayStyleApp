package com.example.kaouzaystyle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val cartItems: List<CartItem>,
    private val onQuantityChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.cartItemImage)
        val name: TextView = view.findViewById(R.id.cartItemName)
        val price: TextView = view.findViewById(R.id.cartItemPrice)
        val size: TextView = view.findViewById(R.id.cartItemSize)
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        val btnIncrease: TextView = view.findViewById(R.id.btnIncrease) // TextView
        val btnDecrease: TextView = view.findViewById(R.id.btnDecrease) // TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.image.setImageResource(item.imageResId)
        holder.name.text = item.name
        holder.price.text = "${item.price * item.quantity} MAD"
        holder.size.text = "Taille : ${item.size}"
        holder.tvQuantity.text = item.quantity.toString()

        holder.btnIncrease.setOnClickListener {
            item.quantity++
            holder.tvQuantity.text = item.quantity.toString()
            holder.price.text = "${item.price * item.quantity} MAD"
            onQuantityChanged()
        }

        holder.btnDecrease.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                holder.tvQuantity.text = item.quantity.toString()
                holder.price.text = "${item.price * item.quantity} MAD"
                onQuantityChanged()
            }
        }
    }
}
