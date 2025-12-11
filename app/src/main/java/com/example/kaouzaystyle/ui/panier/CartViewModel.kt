package com.example.kaouzaystyle.ui.panier

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kaouzaystyle.data.local.entity.ProductCart
import com.example.kaouzaystyle.data.repository.CartRepository
import com.example.kaouzaystyle.util.NotificationHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// On utilise AndroidViewModel pour avoir accès au 'application context' pour les notifs
class CartViewModel(application: Application, private val repository: CartRepository) : AndroidViewModel(application) {

    val cartItems = repository.getCartItems().asLiveData()

    // --- AJOUT : Observer le total pour la notification de seuil ---
    init {
        viewModelScope.launch {
            repository.getCartItems().collectLatest { items ->
                val total = items.sumOf { it.price * it.quantity }
                // Exemple : Seuil à 1000 MAD
                if (total > 1000) {
                    NotificationHelper.sendThresholdNotification(application.applicationContext, total)
                }
            }
        }
    }
    // ----------------------------------------------------------------

    fun addToCart(item: ProductCart) {
        viewModelScope.launch { repository.addToCart(item) }
    }

    fun clearCart() {
        viewModelScope.launch { repository.clearCart() }
    }
}