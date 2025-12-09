package com.example.kaouzaystyle.data.repository

import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.data.local.entity.User

class UserRepository(private val db: AppDatabase) {

    // On appelle la fonction du DAO (registerUser)
    suspend fun saveUser(user: User) {
        db.userDao().registerUser(user)
    }

    // IMPORTANT : On doit passer l'email pour savoir quel utilisateur récupérer
    suspend fun getUser(email: String): User? {
        return db.userDao().getUserByEmail(email)
    }

    // Fonction bonus pour la connexion
    suspend fun login(email: String, password: String): User? {
        return db.userDao().login(email, password)
    }
}