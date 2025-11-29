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

            // 1. Validation
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

            val hasAccount = sharedPref.contains("registered_email")
            val registeredEmail = sharedPref.getString("registered_email", "")
            val registeredPassword = sharedPref.getString("registered_password", "")

            // Exemple reel d'utilisation d'une VARIABLE NULLABLE
            // Ici le nom de l’utilisateur peut être NULL si non enregistré
            val registeredNameNullable: String? =
                sharedPref.getString("registered_name", null)

            // On utilise l'opérateur Elvis pour donner un nom par défaut
            val finalName = registeredNameNullable ?: "Utilisateur"

            // 2. Aucun compte
            if (!hasAccount) {
                errorEmail.text = "Aucun compte n'existe. Veuillez vous inscrire."
                errorEmail.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // 3. Email incorrect
            if (emailInput != registeredEmail) {
                errorEmail.text = "Cet email ne correspond pas au compte enregistré."
                errorEmail.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // 4. Mot de passe incorrect
            if (passwordInput != registeredPassword) {
                errorPassword.text = "Mot de passe incorrect"
                errorPassword.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // 5. Succès — finalName utilise une variable nullable
            Toast.makeText(this, "Bienvenue $finalName !", Toast.LENGTH_SHORT).show()

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
