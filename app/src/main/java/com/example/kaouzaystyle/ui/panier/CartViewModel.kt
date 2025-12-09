package com.example.kaouzaystyle.ui.panier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.example.kaouzaystyle.data.local.entity.ProductCart
import com.example.kaouzaystyle.data.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    val cartItems = repository.getCartItems().asLiveData()

    fun addToCart(item: ProductCart) {
        viewModelScope.launch { repository.addToCart(item) }
    }

    fun clearCart() {
        viewModelScope.launch { repository.clearCart() } // utilise la fonction publique
    }
}
