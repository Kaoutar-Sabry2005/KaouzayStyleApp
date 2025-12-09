package com.example.kaouzaystyle.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kaouzaystyle.data.local.dao.CartDao
import com.example.kaouzaystyle.data.local.dao.UserDao // Ajout
import com.example.kaouzaystyle.data.local.entity.ProductCart
import com.example.kaouzaystyle.data.local.entity.User // Ajout

// Ajout de User::class dans entities et version passe à 2 (ou +1 par rapport à avant)
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