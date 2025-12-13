package com.example.kaouzaystyle.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kaouzaystyle.R
import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.ui.home.HomeActivity
import com.example.kaouzaystyle.ui.signup.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    // On utilise '?' pour dire que ça peut être null (si Firebase ne marche pas)
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // --- PROTECTION CONTRE LE CRASH ---
        try {
            auth = FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Log.e("LoginActivity", "Firebase n'est pas configuré correctement : ${e.message}")
            // On continue sans Firebase, auth reste null
        }

        val emailEdit = findViewById<EditText>(R.id.editEmail)
        val passwordEdit = findViewById<EditText>(R.id.editPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtForgotPassword = findViewById<TextView>(R.id.txtForgotPassword)
        val txtCreateAccount = findViewById<TextView>(R.id.txtCreateAccount)
        val errorEmail = findViewById<TextView>(R.id.errorEmailLogin)
        val errorPassword = findViewById<TextView>(R.id.errorPasswordLogin)

        val db = AppDatabase.getInstance(this)

        btnLogin.setOnClickListener {
            val emailInput = emailEdit.text.toString().trim()
            val passwordInput = passwordEdit.text.toString()

            errorEmail.visibility = View.GONE
            errorPassword.visibility = View.GONE

            if (emailInput.isEmpty()) {
                errorEmail.text = "Veuillez remplir l'email"
                errorEmail.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (passwordInput.isEmpty()) {
                errorPassword.text = "Veuillez remplir le mot de passe"
                errorPassword.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Si Firebase est disponible, on tente la connexion
            if (auth != null) {
                auth!!.signInWithEmailAndPassword(emailInput, passwordInput)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Connexion Firebase réussie !", Toast.LENGTH_SHORT).show()
                            goToHome()
                        } else {
                            // Échec Firebase -> Tentative Locale
                            loginWithRoom(db, emailInput, passwordInput, errorPassword)
                        }
                    }
            } else {
                // Si Firebase n'est pas dispo (auth est null), on passe directement en Local
                loginWithRoom(db, emailInput, passwordInput, errorPassword)
            }
        }

        txtForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        txtCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    // Fonction séparée pour gérer la connexion locale (Room)
    private fun loginWithRoom(db: AppDatabase, email: String, pass: String, errorView: TextView) {
        lifecycleScope.launch {
            val user = db.userDao().login(email, pass)
            if (user != null) {
                Toast.makeText(this@LoginActivity, "Connexion locale : Bienvenue ${user.name} !", Toast.LENGTH_SHORT).show()

                val sharedPref = getSharedPreferences("UserProfile", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putBoolean("is_logged_in", true)
                editor.putString("current_user_email", user.email)
                editor.putString("current_user_name", user.name)
                editor.apply()

                goToHome()
            } else {
                errorView.text = "Email ou mot de passe incorrect (Local)"
                errorView.visibility = View.VISIBLE
            }
        }
    }

    private fun goToHome() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}