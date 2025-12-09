package com.example.kaouzaystyle.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kaouzaystyle.data.local.entity.ProductCart
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: ProductCart)

    @Query("SELECT * FROM cart")
    fun getCartItems(): Flow<List<ProductCart>>

    @Query("DELETE FROM cart")
    suspend fun clearCart()
}
