package com.example.kaouzaystyle.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kaouzaystyle.data.local.entity.ProductFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    // Ajouter aux favoris
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(product: ProductFavorite)

    // Supprimer des favoris (par nom, c'est plus simple ici)
    @Query("DELETE FROM favorites WHERE name = :productName")
    suspend fun removeFavoriteByName(productName: String)

    // Récupérer toute la liste (pour la page Favoris)
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<ProductFavorite>>

    // Vérifier si un produit est déjà en favoris (pour colorier le cœur en rouge)
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE name = :productName LIMIT 1)")
    suspend fun isFavorite(productName: String): Boolean
}