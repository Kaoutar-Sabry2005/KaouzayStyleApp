package com.example.kaouzaystyle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Récupère le bouton depuis le layout (assure-toi que le bouton a bien l'id "btn_se_connecter" dans activity_main.xml)
        val btnSeConnecter: Button = findViewById(R.id.btn_se_connecter)

        // Quand on clique sur le bouton, on ouvre la page de connexion (LoginActivity)
        btnSeConnecter.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            // Optionnel : finish() empêche de revenir sur la page d’accueil avec le bouton retour
            // finish()

        }
    }
}
