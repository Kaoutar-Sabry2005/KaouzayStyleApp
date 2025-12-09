package com.example.kaouzaystyle.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // Nécessaire pour les coroutines
import com.example.kaouzaystyle.R
import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.data.local.entity.User
import com.example.kaouzaystyle.ui.login.LoginActivity
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val editName = findViewById<EditText>(R.id.editName)
        val editEmail = findViewById<EditText>(R.id.editEmailSignUp)
        val editPassword = findViewById<EditText>(R.id.editPasswordSignUp)
        val editConfirmPassword = findViewById<EditText>(R.id.editConfirmPassword)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val txtAlreadyHaveAccount = findViewById<TextView>(R.id.txtAlreadyHaveAccount)

        val errorName = findViewById<TextView>(R.id.errorName)
        val errorEmail = findViewById<TextView>(R.id.errorEmail)
        val errorPassword = findViewById<TextView>(R.id.errorPassword)
        val errorConfirmPassword = findViewById<TextView>(R.id.errorConfirmPassword)

        // Instance de la DB
        val db = AppDatabase.getInstance(this)

        btnSignUp.setOnClickListener {
            val name = editName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString()
            val confirmPassword = editConfirmPassword.text.toString()

            // Reset erreurs
            errorName.visibility = View.GONE
            errorEmail.visibility = View.GONE
            errorPassword.visibility = View.GONE
            errorConfirmPassword.visibility = View.GONE

            var isValid = true

            if (name.isEmpty()) {
                errorName.text = "Veuillez entrer votre nom"
                errorName.visibility = View.VISIBLE
                isValid = false
            }

            if (email.isEmpty()) {
                errorEmail.text = "Veuillez entrer une adresse e-mail"
                errorEmail.visibility = View.VISIBLE
                isValid = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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
                errorConfirmPassword.text = "Veuillez confirmer votre mot de passe"
                errorConfirmPassword.visibility = View.VISIBLE
                isValid = false
            } else if (password != confirmPassword) {
                errorConfirmPassword.text = "Les mots de passe ne correspondent pas"
                errorConfirmPassword.visibility = View.VISIBLE
                isValid = false
            }

            if (isValid) {
                // --- INSERTION DANS ROOM ---
                lifecycleScope.launch {
                    // 1. Vérifier si l'email existe déjà
                    val existingUser = db.userDao().getUserByEmail(email)
                    if (existingUser != null) {
                        errorEmail.text = "Cet email est déjà utilisé"
                        errorEmail.visibility = View.VISIBLE
                    } else {
                        // 2. Créer l'utilisateur
                        val newUser = User(
                            name = name,
                            email = email,
                            password = password
                        )
                        db.userDao().registerUser(newUser)

                        Toast.makeText(this@SignUpActivity, "Inscription réussie !", Toast.LENGTH_SHORT).show()

                        // Redirection
                        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        txtAlreadyHaveAccount.setOnClickListener {
            finish()
        }
    }
}