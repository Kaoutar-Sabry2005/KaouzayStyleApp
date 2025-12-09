package com.example.kaouzaystyle.data.local.entity

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