package com.example.kaouzaystyle

// Gestion globale du panier
object CartManager {
    val cartItems = mutableListOf<CartItem>()

    fun addToCart(item: CartItem) {
        cartItems.add(item)
    }

    fun clearCart() {
        cartItems.clear()
    }
}
// Représentation d'un produit dans le panier
data class CartItem(
    val name: String,
    val price: Double,
    val imageResId: Int,
    val size: String,
    val color: Int,
    var quantity: Int = 1
)
