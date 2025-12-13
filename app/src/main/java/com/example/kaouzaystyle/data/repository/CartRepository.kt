package com.example.kaouzaystyle.data.repository

import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.data.local.entity.ProductCart

class CartRepository(private val db: AppDatabase) {

    // Fonction pour récupérer tous les articles dans le panier
    fun getCartItems() = db.cartDao().getCartItems()

    // Fonction pour ajouter un article au panier
    suspend fun addToCart(item: ProductCart) = db.cartDao().addToCart(item)

    // Fonction pour vider complètement le panier
    suspend fun clearCart() = db.cartDao().clearCart()
}
