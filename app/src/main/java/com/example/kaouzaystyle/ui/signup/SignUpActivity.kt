package com.example.kaouzaystyle.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kaouzaystyle.R
import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.data.local.entity.User
import com.example.kaouzaystyle.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // --- PROTECTION CRASH FIREBASE ---
        try {
            auth = FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Log.e("SignUpActivity", "Firebase erreur : ${e.message}")
        }

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

        val db = AppDatabase.getInstance(this)

        btnSignUp.setOnClickListener {
            val name = editName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString()
            val confirmPassword = editConfirmPassword.text.toString()

            // 1. Réinitialiser les erreurs
            errorName.visibility = View.GONE
            errorEmail.visibility = View.GONE
            errorPassword.visibility = View.GONE
            errorConfirmPassword.visibility = View.GONE

            var isValid = true

            // 2. Vérifications
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
                errorEmail.text = "Email invalide"
                errorEmail.visibility = View.VISIBLE
                isValid = false
            }

            if (password.isEmpty()) {
                errorPassword.text = "Mot de passe requis"
                errorPassword.visibility = View.VISIBLE
                isValid = false
            } else if (password.length < 6) {
                errorPassword.text = "Min. 6 caractères"
                errorPassword.visibility = View.VISIBLE
                isValid = false
            }

            if (confirmPassword != password) {
                errorConfirmPassword.text = "Les mots de passe ne correspondent pas"
                errorConfirmPassword.visibility = View.VISIBLE
                isValid = false
            }

            // 3. Exécution UNIQUEMENT si isValid est true
            if (isValid) {
                // Essai Firebase
                if (auth != null) {
                    auth!!.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Compte Firebase créé !", Toast.LENGTH_SHORT).show()
                                saveToRoomAndExit(db, name, email, password)
                            } else {
                                // Si échec Firebase, on tente quand même en Local (pour le prof)
                                saveToRoomAndExit(db, name, email, password)
                            }
                        }
                } else {
                    // Pas de Firebase -> Local direct
                    saveToRoomAndExit(db, name, email, password)
                }
            }
            // ATTENTION : Ne rien mettre ici (pas de startActivity), sinon ça s'exécute même en cas d'erreur !
        }

        txtAlreadyHaveAccount.setOnClickListener {
            finish() // Retourne à la page de connexion
        }
    }

    private fun saveToRoomAndExit(db: AppDatabase, name: String, email: String, pass: String) {
        lifecycleScope.launch {
            // Vérifier doublon local
            val existing = db.userDao().getUserByEmail(email)
            if (existing != null) {
                Toast.makeText(this@SignUpActivity, "Cet email existe déjà (Local)", Toast.LENGTH_SHORT).show()
            } else {
                val newUser = User(name = name, email = email, password = pass)
                db.userDao().registerUser(newUser)

                Toast.makeText(this@SignUpActivity, "Inscription réussie !", Toast.LENGTH_SHORT).show()

                // C'est ICI qu'on change de page, après le succès de l'enregistrement
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                // On efface l'historique pour ne pas revenir sur SignUp en faisant "Retour"
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}