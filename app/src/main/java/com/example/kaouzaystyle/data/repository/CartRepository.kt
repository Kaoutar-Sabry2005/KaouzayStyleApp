package com.example.kaouzaystyle.data.repository

import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.data.local.entity.ProductCart

class CartRepository(private val db: AppDatabase) {
    fun getCartItems() = db.cartDao().getCartItems()
    suspend fun addToCart(item: ProductCart) = db.cartDao().addToCart(item)
    suspend fun clearCart() = db.cartDao().clearCart() // ajoute cette fonction

}
