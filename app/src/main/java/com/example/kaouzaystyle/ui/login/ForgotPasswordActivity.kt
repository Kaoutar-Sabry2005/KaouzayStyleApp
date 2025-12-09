package com.example.kaouzaystyle.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kaouzaystyle.R

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Récupérer les vues
        val iconClose = findViewById<ImageView>(R.id.icon_close_forgot_password)
        val editEmail = findViewById<EditText>(R.id.editEmailForgotPassword)
        val errorEmail = findViewById<TextView>(R.id.errorEmailForgotPassword)
        val btnSendResetLink = findViewById<Button>(R.id.btnSendResetLink)
        val txtBackToLogin = findViewById<TextView>(R.id.txtBackToLogin)

        // Gérer le clic sur l'icône de fermeture
        iconClose.setOnClickListener {
            finish() // Retourne à l'activité précédente (Login)
        }

        // Gérer le clic sur le bouton "Envoyer le lien"
        btnSendResetLink.setOnClickListener {
            val email = editEmail.text.toString().trim()

            // Cacher le message d'erreur avant validation
            errorEmail.visibility = View.GONE
            var isValid = true

            if (email.isEmpty()) {
                errorEmail.text = "Veuillez entrer votre adresse e-mail"
                errorEmail.visibility = View.VISIBLE
                isValid = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                errorEmail.text = "Veuillez entrer une adresse e-mail valide"
                errorEmail.visibility = View.VISIBLE
                isValid = false
            }

            if (isValid) {
                Toast.makeText(this, "Un lien de réinitialisation a été envoyé à $email", Toast.LENGTH_LONG).show()
                finish() // Retourne à l'écran de connexion après l'envoi du lien
            }
        }

        // Gérer le clic sur "Retour à la connexion"
        txtBackToLogin.setOnClickListener {
            finish() // Retourne à l'activité de connexion
        }
    }
}