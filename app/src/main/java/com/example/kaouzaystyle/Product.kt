package com.example.kaouzaystyle

import java.io.Serializable

// Classe pour une variante (Couleur + Image associée)
data class ProductVariant(
    val colorInt: Int,
    val imageResId: Int
) : Serializable

// Classe principale Produit
data class Product(
    val name: String,
    val price: String,
    val image: Int,          // Image par défaut (utilisée dans la liste d'accueil)
    val description: String,
    val variants: List<ProductVariant>, // Liste des variantes de couleurs
    val availableSizes: List<String>    // Liste des tailles (S, M, 36, 40...)
) : Serializable