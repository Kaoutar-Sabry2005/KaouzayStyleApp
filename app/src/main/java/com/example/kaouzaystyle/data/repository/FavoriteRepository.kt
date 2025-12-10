package com.example.kaouzaystyle.data.repository

import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.data.local.entity.ProductFavorite

class FavoriteRepository(private val db: AppDatabase) {

    suspend fun addFavorite(product: ProductFavorite) = db.favoriteDao().addFavorite(product)

    suspend fun removeFavorite(name: String) = db.favoriteDao().removeFavoriteByName(name)

    suspend fun isFavorite(name: String): Boolean = db.favoriteDao().isFavorite(name)

    fun getAllFavorites() = db.favoriteDao().getAllFavorites()
}