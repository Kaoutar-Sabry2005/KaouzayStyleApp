package com.example.kaouzaystyle.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_cart")
data class ProductCart(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String, // <--- CHANGEMENT ICI : String au lieu de Int (imageResId)
    val size: String,
    val colorInt: Int
)
/*package com.example.kaouzaystyle.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class ProductCart(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double,
    var quantity: Int,
    val imageResId: Int,
    val size: String,
    val colorInt: Int
)
*/
