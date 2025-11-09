package com.example.kaouzaystyle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Views requises (présentes dans ton layout de base)
        val email = findViewById<EditText?>(R.id.editEmail)
        val password = findViewById<EditText?>(R.id.editPassword)
        val btnLogin = findViewById<Button?>(R.id.btnLogin)
        val txtForgotPassword = findViewById<TextView?>(R.id.txtForgotPassword)

        // Views optionnelles — si tu as modifié le XML pour les ajouter
        val txtCreateAccount = findViewById<TextView?>(R.id.txtCreateAccount)

        // Login
        btnLogin?.setOnClickListener {
            val emailText = email?.text?.toString() ?: ""
            val passwordText = password?.text?.toString() ?: ""

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: Vérification réelle (Firebase/SQLite) à implémenter plus tard
                Toast.makeText(this, "Connexion réussie ✅", Toast.LENGTH_SHORT).show()

                // Exemple : redirection vers MainActivity (assure-toi que MainActivity existe)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        // Mot de passe oublié
        txtForgotPassword?.setOnClickListener {
            Toast.makeText(this, "Fonctionnalité à venir...", Toast.LENGTH_SHORT).show()
        }

        // Create account (laisse la fonction vide pour Kaoutar)

        txtCreateAccount.setOnClickListener {
            // TODO: votre binôme réalisera la page d'inscription ici
            // Exemple de code à remplir plus tard :
            // val intent = Intent(this, RegisterActivity::class.java)
            // startActivity(intent)

            Toast.makeText(this, "Page d'inscription à venir...", Toast.LENGTH_SHORT).show()
        }


    }
}
