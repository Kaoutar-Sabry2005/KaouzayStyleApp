package com.example.kaouzaystyle.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kaouzaystyle.data.local.dao.CartDao
import com.example.kaouzaystyle.data.local.dao.FavoriteDao // <-- AJOUT
import com.example.kaouzaystyle.data.local.dao.UserDao
import com.example.kaouzaystyle.data.local.entity.ProductCart
import com.example.kaouzaystyle.data.local.entity.ProductFavorite // <-- AJOUT
import com.example.kaouzaystyle.data.local.entity.User

// Déclaration de la base de données avec les entités et la version
@Database(entities = [ProductCart::class, User::class, ProductFavorite::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Méthode pour accéder aux opérations sur le panier
    abstract fun cartDao(): CartDao

    // Méthode pour accéder aux opérations sur les utilisateurs
    abstract fun userDao(): UserDao

    // Méthode pour accéder aux opérations sur les favoris
    abstract fun favoriteDao(): FavoriteDao // <-- AJOUT

    companion object {
        // Instance unique de la base de données pour éviter plusieurs créations
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Fonction pour récupérer l'instance unique de la base de données
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kaouzay_db" // Nom de la base de données
                )
                    .fallbackToDestructiveMigration() // Supprime les données si la version change (pratique en développement)
                    .build()
                INSTANCE = instance // Sauvegarde l'instance
                instance
            }
        }
    }
}

