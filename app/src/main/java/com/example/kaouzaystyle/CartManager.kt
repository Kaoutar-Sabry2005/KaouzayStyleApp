package com.example.kaouzaystyle

import java.io.Serializable

// Modèle de données
data class CartItem(
    val name: String,
    val price: Double,
    val imageResId: Int,
    val size: String,
    val colorInt: Int,
    var quantity: Int = 1
) : Serializable

// Singleton (Base de données temporaire en mémoire)
object CartManager {
    val cartItems = mutableListOf<CartItem>()

    fun addToCart(item: CartItem) {
        // On vérifie si l'article identique existe déjà
        val existing = cartItems.find {
            it.name == item.name &&
                    it.size == item.size &&
                    it.colorInt == item.colorInt
        }

        if (existing != null) {
            existing.quantity++
        } else {
            cartItems.add(item)
        }
    }

    fun clearCart() {
        cartItems.clear()
    }
}
