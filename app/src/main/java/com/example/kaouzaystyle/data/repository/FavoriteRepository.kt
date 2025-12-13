package com.example.kaouzaystyle.data.repository

import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.data.local.entity.ProductFavorite

// Repository pour gérer les favoris dans la base de données
class FavoriteRepository(private val db: AppDatabase) {

    // Ajoute un produit aux favoris
    suspend fun addFavorite(product: ProductFavorite) = db.favoriteDao().addFavorite(product)

    // Supprime un produit des favoris en fonction de son nom
    suspend fun removeFavorite(name: String) = db.favoriteDao().removeFavoriteByName(name)

    // Vérifie si un produit est déjà dans les favoris
    suspend fun isFavorite(name: String): Boolean = db.favoriteDao().isFavorite(name)

    // Récupère tous les produits favoris
    fun getAllFavorites() = db.favoriteDao().getAllFavorites()
}
