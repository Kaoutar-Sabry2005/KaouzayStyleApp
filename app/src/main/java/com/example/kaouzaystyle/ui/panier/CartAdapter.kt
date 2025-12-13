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

// Adapter pour afficher les articles du panier dans un RecyclerView
class CartAdapter(
    private var cartItems: List<ProductCart>, // Liste des articles du panier
    private val onQuantityChanged: (ProductCart) -> Unit // Action quand la quantité change
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // ViewHolder pour chaque élément du panier
    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.cartItemImage) // Image du produit
        val name: TextView = view.findViewById(R.id.cartItemName) // Nom du produit
        val price: TextView = view.findViewById(R.id.cartItemPrice) // Prix total pour la quantité
        val size: TextView = view.findViewById(R.id.cartItemSize) // Taille du produit
        val colorView: View = view.findViewById(R.id.cartItemColorView) // Affichage de la couleur
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity) // Quantité du produit
        val btnIncrease: TextView = view.findViewById(R.id.btnIncrease) // Bouton pour augmenter la quantité
        val btnDecrease: TextView = view.findViewById(R.id.btnDecrease) // Bouton pour diminuer la quantité
    }

    // Crée un nouveau ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    // Retourne le nombre d'articles dans le panier
    override fun getItemCount(): Int = cartItems.size

    // Remplit les données dans chaque élément du panier
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]

        holder.name.text = item.name // Nom du produit
        holder.size.text = "Taille : ${item.size}" // Taille affichée

        // Charger l'image du produit avec Glide
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.ic_launcher_background) // Image par défaut
            .error(R.drawable.ic_launcher_background) // Image en cas d'erreur
            .into(holder.image)

        holder.tvQuantity.text = item.quantity.toString() // Quantité
        holder.price.text = "%.2f MAD".format(item.price * item.quantity) // Prix total

        // Affichage de la couleur du produit dans un cercle
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(item.colorInt)
        shape.setStroke(2, Color.LTGRAY)
        holder.colorView.background = shape

        // Augmenter la quantité
        holder.btnIncrease.setOnClickListener {
            val newItem = item.copy(quantity = item.quantity + 1)
            onQuantityChanged(newItem)
        }

        // Diminuer la quantité si > 1
        holder.btnDecrease.setOnClickListener {
            if (item.quantity > 1) {
                val newItem = item.copy(quantity = item.quantity - 1)
                onQuantityChanged(newItem)
            }
        }
    }

    // Met à jour la liste des articles du panier
    fun submitList(newList: List<ProductCart>) {
        cartItems = newList
        notifyDataSetChanged()
    }
}
