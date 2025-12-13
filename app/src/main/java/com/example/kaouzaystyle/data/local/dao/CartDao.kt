package com.example.kaouzaystyle.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kaouzaystyle.data.local.entity.ProductCart
import kotlinx.coroutines.flow.Flow

// Un DAO contient toutes les méthodes pour accéder à la base de données
@Dao
interface CartDao {

    // Récupère tous les articles du panier
    @Query("SELECT * FROM product_cart")
    fun getCartItems(): Flow<List<ProductCart>>

    // Ajoute un article au panier
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: ProductCart)

    // Vide complètement le panier
    @Query("DELETE FROM product_cart")
    suspend fun clearCart()
}
