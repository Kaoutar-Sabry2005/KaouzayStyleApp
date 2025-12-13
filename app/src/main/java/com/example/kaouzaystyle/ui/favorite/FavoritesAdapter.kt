package com.example.kaouzaystyle.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kaouzaystyle.R
import com.example.kaouzaystyle.data.local.entity.ProductFavorite

// Adapter pour afficher la liste des produits favoris dans un RecyclerView
class FavoritesAdapter(
    private var favoritesList: List<ProductFavorite>, // Liste des favoris
    private val onDeleteClick: (ProductFavorite) -> Unit // Action lors de la suppression d'un favori
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    // ViewHolder pour chaque élément de la liste
    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgFavProduct) // Image du produit
        val name: TextView = view.findViewById(R.id.txtFavName) // Nom du produit
        val price: TextView = view.findViewById(R.id.txtFavPrice) // Prix du produit
        val btnDelete: ImageView = view.findViewById(R.id.btnDeleteFav) // Bouton pour supprimer
    }

    // Crée un nouveau ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false) // Lie le layout de l'item
        return FavoriteViewHolder(view)
    }

    // Retourne le nombre d'éléments dans la liste
    override fun getItemCount(): Int = favoritesList.size

    // Remplit les données dans chaque élément de la liste
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = favoritesList[position]

        holder.name.text = item.name // Affiche le nom
        holder.price.text = "%.2f MAD".format(item.price) // Affiche le prix formaté

        // Charger l'image avec Glide
        Glide.with(holder.itemView.context)
            .load(item.imageUrl) // URL de l'image
            .placeholder(R.drawable.ic_launcher_background) // Image par défaut si non chargée
            .into(holder.img)

        // Clic sur le bouton supprimer pour enlever le favori
        holder.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }
    }

    // Met à jour la liste des favoris et rafraîchit le RecyclerView
    fun submitList(newList: List<ProductFavorite>) {
        favoritesList = newList
        notifyDataSetChanged()
    }
}
