package com.example.kaouzaystyle.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // Pour les coroutines
import com.example.kaouzaystyle.R
import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.ui.home.HomeActivity
import com.example.kaouzaystyle.ui.signup.SignUpActivity
import kotlinx.coroutines.launch

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

        val db = AppDatabase.getInstance(this)

        btnLogin.setOnClickListener {
            val emailInput = emailEdit.text.toString().trim()
            val passwordInput = passwordEdit.text.toString()

            errorEmail.visibility = View.GONE
            errorPassword.visibility = View.GONE

            // Validation simple
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

            // --- VÉRIFICATION ROOM ---
            lifecycleScope.launch {
                val user = db.userDao().login(emailInput, passwordInput)

                if (user != null) {
                    Toast.makeText(this@LoginActivity, "Bienvenue ${user.name} !", Toast.LENGTH_SHORT).show()

                    val sharedPref = getSharedPreferences("UserProfile", MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putBoolean("is_logged_in", true)
                    editor.putString("current_user_email", user.email)
                    editor.putString("current_user_name", user.name)
                    editor.apply()

                    // Redirection vers Home
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()

                } else {

                    val userByEmail = db.userDao().getUserByEmail(emailInput)

                    if (userByEmail == null) {
                        errorEmail.text = "Aucun compte n'existe avec cet email."
                        errorEmail.visibility = View.VISIBLE
                    } else {
                        errorPassword.text = "Mot de passe incorrect"
                        errorPassword.visibility = View.VISIBLE
                    }
                }
            }
        }

        txtForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        txtCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}