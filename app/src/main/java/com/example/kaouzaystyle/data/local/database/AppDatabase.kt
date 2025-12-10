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

// Ajoute ProductFavorite::class et augmente la version
@Database(entities = [ProductCart::class, User::class, ProductFavorite::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao // <-- AJOUT

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kaouzay_db"
                )
                    .fallbackToDestructiveMigration() // Va supprimer les données si la version change (utile en dév)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
/*
package com.example.kaouzaystyle.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kaouzaystyle.data.local.dao.CartDao
import com.example.kaouzaystyle.data.local.dao.UserDao // Ajout
import com.example.kaouzaystyle.data.local.entity.ProductCart
import com.example.kaouzaystyle.data.local.entity.User // Ajout

@Database(entities = [ProductCart::class, User::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    abstract fun userDao(): UserDao // Ajout

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kaouzay_db"
                )
                    .fallbackToDestructiveMigration() // Important pour éviter le crash si on change la structure
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
 */