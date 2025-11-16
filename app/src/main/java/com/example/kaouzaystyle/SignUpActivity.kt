package com.example.kaouzaystyle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.View // Import pour View.GONE et View.VISIBLE

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Récupérer les vues des champs
        val editName = findViewById<EditText>(R.id.editName)
        val editEmail = findViewById<EditText>(R.id.editEmailSignUp)
        val editPassword = findViewById<EditText>(R.id.editPasswordSignUp)
        val editConfirmPassword = findViewById<EditText>(R.id.editConfirmPassword)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val txtAlreadyHaveAccount = findViewById<TextView>(R.id.txtAlreadyHaveAccount)

        // Récupérer les vues des messages d'erreur
        val errorName = findViewById<TextView>(R.id.errorName)
        val errorEmail = findViewById<TextView>(R.id.errorEmail)
        val errorPassword = findViewById<TextView>(R.id.errorPassword)
        val errorConfirmPassword = findViewById<TextView>(R.id.errorConfirmPassword)


        // Gérer le clic sur le bouton "S'inscrire"
        btnSignUp.setOnClickListener {
            val name = editName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString()
            val confirmPassword = editConfirmPassword.text.toString()

            // Cacher tous les messages d'erreur avant une nouvelle validation
            errorName.visibility = View.GONE
            errorEmail.visibility = View.GONE
            errorPassword.visibility = View.GONE
            errorConfirmPassword.visibility = View.GONE

            var isValid = true // Flag pour suivre l'état de la validation

            // Valider les champs
            if (name.isEmpty()) {
                errorName.text = "Veuillez entrer votre nom"
                errorName.visibility = View.VISIBLE
                isValid = false
            }

            if (email.isEmpty()) {
                errorEmail.text = "Veuillez entrer une adresse e-mail"
                errorEmail.visibility = View.VISIBLE
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                errorEmail.text = "Veuillez entrer une adresse e-mail valide"
                errorEmail.visibility = View.VISIBLE
                isValid = false
            }

            if (password.isEmpty()) {
                errorPassword.text = "Veuillez entrer un mot de passe"
                errorPassword.visibility = View.VISIBLE
                isValid = false
            } else if (password.length < 6) {
                errorPassword.text = "Le mot de passe doit contenir au moins 6 caractères"
                errorPassword.visibility = View.VISIBLE
                isValid = false
            }

            if (confirmPassword.isEmpty()) {
                // Pas de message spécifique si vide, car la validation de non-correspondance le gérera
                // ou on peut mettre un message générique si on veut absolument un message ici.
                // Pour l'instant, la validation password != confirmPassword couvrira cela.
                errorConfirmPassword.text = "Veuillez confirmer votre mot de passe"
                errorConfirmPassword.visibility = View.VISIBLE
                isValid = false
            } else if (password != confirmPassword) {
                errorConfirmPassword.text = "Les mots de passe ne correspondent pas"
                errorConfirmPassword.visibility = View.VISIBLE
                isValid = false
            }

            // Si toutes les validations passent
            if (isValid) {
                Toast.makeText(this, "Inscription réussie ! Veuillez vous connecter.", Toast.LENGTH_LONG).show()

                // Rediriger vers l'écran de connexion après une inscription réussie
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

        // Gérer le clic sur "Vous avez déjà un compte ? Se connecter"
        txtAlreadyHaveAccount.setOnClickListener {
            finish() // Retourne simplement à l'activité de connexion
        }
    }
}