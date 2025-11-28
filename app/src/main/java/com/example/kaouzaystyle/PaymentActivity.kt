package com.example.kaouzaystyle

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Champs du formulaire
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val paymentGroup = findViewById<RadioGroup>(R.id.paymentMethodGroup)
        val btnValidate = findViewById<Button>(R.id.btnValidateOrder)

        // TextView pour afficher le total
        val tvTotal = findViewById<TextView>(R.id.tvTotalPayment)

        // Récupérer le total envoyé depuis PanierActivity
        val totalAmount = intent.getStringExtra("TOTAL_AMOUNT") ?: "0.00 MAD"
        tvTotal.text = "Total à payer: $totalAmount"

        btnValidate.setOnClickListener {
            val name = etFullName.text.toString()
            val address = etAddress.text.toString()
            val phone = etPhone.text.toString()
            val paymentSelected = paymentGroup.checkedRadioButtonId

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || paymentSelected == -1) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tout est bon → Confirmation
            val intent = Intent(this, SuccessActivity::class.java)
            startActivity(intent)
        }
    }
}
