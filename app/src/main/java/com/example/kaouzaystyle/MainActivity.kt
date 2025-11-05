package com.example.kaouzaystyle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSeConnecter: Button = findViewById(R.id.btn_se_connecter)

        btnSeConnecter.setOnClickListener {
            // Logique pour naviguer vers l'écran de Login
            // Zaynab créera LoginActivity.kt
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            // finish() // Optionnel: pour empêcher de revenir à l'écran d'accueil avec le bouton retour
        }
    }
}