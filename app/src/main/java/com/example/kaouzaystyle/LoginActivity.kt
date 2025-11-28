package com.example.kaouzaystyle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEdit = findViewById<EditText>(R.id.editEmail)
        val passwordEdit = findViewById<EditText>(R.id.editPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtForgotPassword = findViewById<TextView>(R.id.txtForgotPassword)
        val txtCreateAccount = findViewById<TextView>(R.id.txtCreateAccount)
        val errorEmail = findViewById<TextView>(R.id.errorEmailLogin)
        val errorPassword = findViewById<TextView>(R.id.errorPasswordLogin)

        btnLogin.setOnClickListener {
            val emailInput = emailEdit.text.toString().trim()
            val passwordInput = passwordEdit.text.toString()

            // Reset erreurs
            errorEmail.visibility = View.GONE
            errorPassword.visibility = View.GONE

            // 1. Validation de base
            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                if(emailInput.isEmpty()) {
                    errorEmail.text = "Veuillez remplir l'email"
                    errorEmail.visibility = View.VISIBLE
                }
                if(passwordInput.isEmpty()) {
                    errorPassword.text = "Veuillez remplir le mot de passe"
                    errorPassword.visibility = View.VISIBLE
                }
                return@setOnClickListener
            }

            // --- LECTURE MÉMOIRE ---
            val sharedPref = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

            // On vérifie si la clé existe vraiment
            val hasAccount = sharedPref.contains("registered_email")
            val registeredEmail = sharedPref.getString("registered_email", "")
            val registeredPassword = sharedPref.getString("registered_password", "")
            val registeredName = sharedPref.getString("registered_name", "Utilisateur")

            // 2. CAS : Aucun compte n'a jamais été créé sur ce téléphone
            if (!hasAccount) {
                errorEmail.text = "Aucun compte n'existe. Veuillez vous inscrire."
                errorEmail.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // 3. CAS : Un compte existe, mais l'email saisi n'est pas le bon
            if (emailInput != registeredEmail) {
                errorEmail.text = "Cet email ne correspond pas au compte enregistré."
                errorEmail.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // 4. CAS : Email bon, mais Mot de passe incorrect
            if (passwordInput != registeredPassword) {
                errorPassword.text = "Mot de passe incorrect"
                errorPassword.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // 5. TOUT EST BON (Succès)
            Toast.makeText(this, "Bienvenue $registeredName !", Toast.LENGTH_SHORT).show()
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
}