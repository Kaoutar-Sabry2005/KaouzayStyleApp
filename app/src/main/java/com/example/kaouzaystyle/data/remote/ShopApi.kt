package com.example.kaouzaystyle.data.remote

import com.example.kaouzaystyle.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Classe qui représente la réponse de l'API contenant la liste des produits
data class ShopResponse(
    val products: List<Product>
)

// Interface définissant les appels à l'API
interface ShopApiService {
    // Récupère les produits depuis le fichier JSON
    @GET("products.json")
    suspend fun getProducts(): ShopResponse
}

// Objet pour configurer Retrofit et créer l'instance de l'API
object RetrofitClient {
    // URL de base de l'API
    private const val BASE_URL = "https://raw.githubusercontent.com/zaynabourchane/myshop-api/refs/heads/main/"

    // Instance unique de ShopApiService créée avec Retrofit
    val instance: ShopApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Définit l'URL de base
            .addConverterFactory(GsonConverterFactory.create()) // Convertit le JSON en objets Kotlin
            .build()
            .create(ShopApiService::class.java) // Crée l'API
    }
}
