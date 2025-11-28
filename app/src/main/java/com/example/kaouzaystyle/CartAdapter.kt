package com.example.kaouzaystyle

import android.graphics.drawable.GradientDrawable
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
        val colorView: View = view.findViewById(R.id.cartItemColorView)

        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        val btnIncrease: TextView = view.findViewById(R.id.btnIncrease)
        val btnDecrease: TextView = view.findViewById(R.id.btnDecrease)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        // Chargement du fichier layout existant
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]

        holder.name.text = item.name
        holder.size.text = "Taille : ${item.size}"
        holder.image.setImageResource(item.imageResId)
        holder.tvQuantity.text = item.quantity.toString()

        val totalPriceItem = item.price * item.quantity
        holder.price.text = "%.2f MAD".format(totalPriceItem)

        // Afficher couleur
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(item.colorInt)
        // Petit bord gris pour que le blanc se voit
        shape.setStroke(2, android.graphics.Color.LTGRAY)
        holder.colorView.background = shape

        holder.btnIncrease.setOnClickListener {
            item.quantity++
            notifyItemChanged(position)
            onQuantityChanged()
        }

        holder.btnDecrease.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                notifyItemChanged(position)
                onQuantityChanged()
            }
        }
    }
}