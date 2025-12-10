package com.example.kaouzaystyle

import android.graphics.Color
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
    val id: String,

    @SerializedName("title")
    val name: String,

    val price: Double,

    @SerializedName("main_image")
    val imageUrl: String,

    val description: String,
    val category: String,

    val variants: List<ProductVariant> = emptyList(),
    val sizes: List<String> = emptyList()
) : Serializable

data class ProductVariant(
    @SerializedName("color")
    val colorHex: String,

    @SerializedName("image")
    val imageUrl: String
) : Serializable {
    val color: Int
        get() = try {
            Color.parseColor(colorHex)
        } catch (e: Exception) {
            Color.GRAY
        }
}