package com.example.kaouzaystyle

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Récupérer la flèche retour
        val btnBack = findViewById<ImageView>(R.id.paymentBackButton)

        // Champs du formulaire
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val paymentGroup = findViewById<RadioGroup>(R.id.paymentMethodGroup)
        val btnValidate = findViewById<Button>(R.id.btnValidateOrder)
        val tvTotal = findViewById<TextView>(R.id.tvTotalPayment)

        // Gestion du bouton retour
        btnBack.setOnClickListener {
            finish() // Revient au panier
        }

        // Récupérer le total
        val totalAmount = intent.getStringExtra("TOTAL_AMOUNT") ?: "0.00 MAD"
        tvTotal.text = "Total à payer : $totalAmount"

        btnValidate.setOnClickListener {
            val name = etFullName.text.toString()
            val address = etAddress.text.toString()
            val phone = etPhone.text.toString()
            val paymentSelected = paymentGroup.checkedRadioButtonId

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || paymentSelected == -1) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tout est bon → On vide le panier et on confirme
            CartManager.clearCart() // Vider le panier après commande !

            val intent = Intent(this, SuccessActivity::class.java)
            // On empêche de revenir en arrière sur le paiement
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}