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

        val email = findViewById<EditText>(R.id.editEmail)
        val password = findViewById<EditText>(R.id.editPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtForgotPassword = findViewById<TextView>(R.id.txtForgotPassword)

        btnLogin.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                // Ici tu peux plus tard vérifier avec Firebase, SQLite, etc.
                Toast.makeText(this, "Connexion réussie ✅", Toast.LENGTH_SHORT).show()

                // Rediriger vers une autre page (ex: HomeActivity)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        txtForgotPassword.setOnClickListener {
            Toast.makeText(this, "Fonctionnalité à venir...", Toast.LENGTH_SHORT).show()
        }
    }
}
