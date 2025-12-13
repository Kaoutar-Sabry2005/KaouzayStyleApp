package com.example.kaouzaystyle

import android.graphics.Color
import com.google.gson.annotations.SerializedName
import java.io.Serializable

// Classe représentant un produit
data class Product(
    val id: String, // Identifiant unique du produit

    @SerializedName("title")
    val name: String, // Nom du produit (provenant de JSON "title")

    val price: Double, // Prix du produit

    @SerializedName("main_image")
    val imageUrl: String, // URL de l'image principale (provenant de JSON "main_image")

    val description: String, // Description du produit
    val category: String, // Catégorie du produit (ex: caftan, djellaba)

    val variants: List<ProductVariant> = emptyList(), // Liste des variantes de couleur
    val sizes: List<String> = emptyList() // Liste des tailles disponibles
) : Serializable // Serializable pour pouvoir passer l'objet via Intent

// Classe représentant une variante de produit (couleur + image)
data class ProductVariant(
    @SerializedName("color")
    val colorHex: String, // Code hexadécimal de la couleur (provenant de JSON "color")

    @SerializedName("image")
    val imageUrl: String // URL de l'image correspondant à cette couleur
) : Serializable {
    // Convertit le code hexadécimal en valeur de couleur Android
    val color: Int
        get() = try {
            Color.parseColor(colorHex)
        } catch (e: Exception) {
            Color.GRAY // Couleur par défaut si le code est invalide
        }
}
