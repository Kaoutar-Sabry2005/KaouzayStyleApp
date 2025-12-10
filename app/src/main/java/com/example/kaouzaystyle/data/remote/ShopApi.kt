package com.example.kaouzaystyle.data.remote

import com.example.kaouzaystyle.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class ShopResponse(
    val products: List<Product>
)

interface ShopApiService {
    @GET("products.json")
    suspend fun getProducts(): ShopResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://raw.githubusercontent.com/zaynabourchane/myshop-api/refs/heads/main/"

    val instance: ShopApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShopApiService::class.java)
    }
}