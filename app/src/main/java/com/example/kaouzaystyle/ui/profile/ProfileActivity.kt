package com.example.kaouzaystyle.ui.profile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kaouzaystyle.R

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val editName = findViewById<EditText>(R.id.editName)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPhone = findViewById<EditText>(R.id.editPhone)
        val editAddress = findViewById<EditText>(R.id.editAddress)
        val btnSave = findViewById<Button>(R.id.btnSaveProfile)
        val btnCancel = findViewById<Button>(R.id.btnCancelProfile)
        val backButton = findViewById<ImageView>(R.id.backButton)

        // 1. Charger les données (On utilise les clés définies dans SignUp)
        val sharedPref = getSharedPreferences("UserProfile", MODE_PRIVATE)

        editName.setText(sharedPref.getString("registered_name", ""))
        editEmail.setText(sharedPref.getString("registered_email", ""))
        editPhone.setText(sharedPref.getString("registered_phone", ""))
        editAddress.setText(sharedPref.getString("registered_address", ""))

        // Retour
        backButton.setOnClickListener { finish() }

        // BOUTON ANNULER
        btnCancel.setOnClickListener {
            finish() // Ferme la page sans enregistrer
        }

        // BOUTON ENREGISTRER
        btnSave.setOnClickListener {
            val newName = editName.text.toString()
            val newEmail = editEmail.text.toString()
            val newPhone = editPhone.text.toString()
            val newAddress = editAddress.text.toString()

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Nom et Email ne peuvent pas être vides", Toast.LENGTH_SHORT).show()
            } else {
                val editor = sharedPref.edit()
                // Mise à jour des mêmes clés
                editor.putString("registered_name", newName)
                editor.putString("registered_email", newEmail)
                editor.putString("registered_phone", newPhone)
                editor.putString("registered_address", newAddress)
                // Attention: Si on change l'email ici, il faudra utiliser le NOUVEAU pour se connecter !
                editor.putString("registered_password", sharedPref.getString("registered_password", "")) // On garde le pass
                editor.apply()

                Toast.makeText(this, "Modifications enregistrées ", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}