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

class FavoritesAdapter(
    private var favoritesList: List<ProductFavorite>,
    private val onDeleteClick: (ProductFavorite) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgFavProduct)
        val name: TextView = view.findViewById(R.id.txtFavName)
        val price: TextView = view.findViewById(R.id.txtFavPrice)
        val btnDelete: ImageView = view.findViewById(R.id.btnDeleteFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = favoritesList.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = favoritesList[position]

        holder.name.text = item.name
        holder.price.text = "%.2f MAD".format(item.price)

        // Charger l'image avec Glide
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.img)

        // Clic sur le cœur (poubelle) pour supprimer
        holder.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }
    }

    fun submitList(newList: List<ProductFavorite>) {
        favoritesList = newList
        notifyDataSetChanged()
    }
}