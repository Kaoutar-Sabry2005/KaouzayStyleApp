package com.example.kaouzaystyle

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Bouton retour
        val btnBack = findViewById<ImageView>(R.id.paymentBackButton)

        // Champs utilisateur
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etPhone = findViewById<EditText>(R.id.etPhone)

        // Paiement
        val paymentGroup = findViewById<RadioGroup>(R.id.paymentMethodGroup)
        val rbCard = findViewById<RadioButton>(R.id.rbCard)
        val rbCOD = findViewById<RadioButton>(R.id.rbCOD)

        // Zone carte bancaire
        val cardInfoLayout = findViewById<LinearLayout>(R.id.cardInfoLayout)
        val etCardNumber = findViewById<EditText>(R.id.etCardNumber)


        // Total paiement
        val tvTotal = findViewById<TextView>(R.id.tvTotalPayment)

        // Bouton valider
        val btnValidate = findViewById<Button>(R.id.btnValidateOrder)


        // Retour
        btnBack.setOnClickListener {
            finish()
        }


        // Afficher total reçu depuis Panier
        val totalAmount = intent.getStringExtra("TOTAL_AMOUNT") ?: "0.00 MAD"
        tvTotal.text = "Total à payer : $totalAmount"


        // Afficher / cacher les champs carte selon sélection
        paymentGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbCard) {
                cardInfoLayout.visibility = LinearLayout.VISIBLE
            } else {
                cardInfoLayout.visibility = LinearLayout.GONE
            }
        }


        // Validation de la commande
        btnValidate.setOnClickListener {

            val name = etFullName.text.toString().trim()
            val address = etAddress.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val paymentSelected = paymentGroup.checkedRadioButtonId

            // Vérification champs obligatoires
            if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || paymentSelected == -1) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Vérification champs carte si sélection carte bancaire
            if (paymentSelected == R.id.rbCard) {

                val cardNumber = etCardNumber.text.toString().trim()


                if (cardNumber.isEmpty()) {
                    Toast.makeText(this, "Veuillez remplir les informations de carte", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (cardNumber.length < 12) {
                    Toast.makeText(this, "Numéro de carte invalide", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


            }

            // Si tout est bon → valider la commande
            CartManager.clearCart()

            val intent = Intent(this, SuccessActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
