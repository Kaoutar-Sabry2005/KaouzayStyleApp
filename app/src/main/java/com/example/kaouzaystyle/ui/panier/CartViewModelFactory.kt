package com.example.kaouzaystyle.ui.panier

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kaouzaystyle.data.repository.CartRepository

// Factory pour créer une instance de CartViewModel avec des paramètres spécifiques
class CartViewModelFactory(
    private val application: Application, // Application pour le ViewModel
    private val repository: CartRepository // Repository pour accéder aux données du panier
) : ViewModelProvider.Factory {

    // Crée le ViewModel demandé
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Vérifie si le ViewModel demandé est CartViewModel
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Crée et retourne une instance de CartViewModel
            return CartViewModel(application, repository) as T
        }
        // Lance une exception si le ViewModel n'est pas reconnu
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
