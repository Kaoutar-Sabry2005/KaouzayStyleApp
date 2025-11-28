package com.example.kaouzaystyle

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // --- Appel de l'exemple nullable ---
        exempleNullable()

        val email = findViewById<EditText>(R.id.editEmail)
        val password = findViewById<EditText>(R.id.editPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtForgotPassword = findViewById<TextView>(R.id.txtForgotPassword)
        val txtCreateAccount = findViewById<TextView>(R.id.txtCreateAccount)

        btnLogin.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString()

            // Vérifier si vide
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Vérifier email valide
            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                Toast.makeText(this, "Veuillez entrer un email valide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Vérifier longueur mot de passe
            if (passwordText.length < 8) {
                Toast.makeText(this, "Le mot de passe doit contenir au moins 8 caractères", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Si tout est valide
            Toast.makeText(this, "Connexion réussie ✅", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        txtForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        txtCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    //  EXEMPLE SIMPLE D’UTILISATION D’UNE VARIABLE NULLABLE
    private fun exempleNullable() {

        // 1) Déclaration d’une variable nullable (peut contenir un texte OU null)
        var message: String? = null

        // 2) Utilisation du safe call ?. pour éviter un crash si message == null
        println("Longueur du message au début : ${message?.length}")
        // Résultat : null

        // 3) On assigne une valeur
        message = "Valeur null"

        // 4) Safe-call quand la variable n’est plus null
        println("Longueur du message après valeur : ${message?.length}")
        // Résultat : 14

        // 5) Utilisation de l’opérateur Elvis ?: pour prévoir une valeur si null
        val affichage = message ?: "Aucun message"
        println("Affichage final : $affichage")
    }
}
