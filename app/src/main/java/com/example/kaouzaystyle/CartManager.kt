package com.example.kaouzaystyle

object CartManager {
    // Liste simple pour stocker les produits ajoutés
    val cartItems = mutableListOf<Product>()

    fun addToCart(product: Product) {
        cartItems.add(product)
    }
}