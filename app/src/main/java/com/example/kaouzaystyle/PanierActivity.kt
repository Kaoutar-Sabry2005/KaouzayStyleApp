package com.example.kaouzaystyle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PanierActivity : AppCompatActivity() {

    private lateinit var subtotalTxt: TextView
    private lateinit var totalTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val recyclerView = findViewById<RecyclerView>(R.id.cartRecyclerView)
        subtotalTxt = findViewById(R.id.subtotalAmount)
        totalTxt = findViewById(R.id.totalAmount)
        val btnCheckout = findViewById<Button>(R.id.checkoutButton)
        val btnBack = findViewById<ImageView>(R.id.cartBackButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        // Chargement adaptateur
        val adapter = CartAdapter(CartManager.cartItems) {
            updateUI() // Rappelé quand qté change
        }
        recyclerView.adapter = adapter

        updateUI()

        btnCheckout.setOnClickListener {
            if (CartManager.cartItems.isEmpty()) {
                Toast.makeText(this, "Le panier est vide", Toast.LENGTH_SHORT).show()
            } else {
                val i = Intent(this, PaymentActivity::class.java)
                i.putExtra("TOTAL_AMOUNT", totalTxt.text.toString())
                startActivity(i)
            }
        }

        btnBack.setOnClickListener { finish() }
        setupBottomMenu()
    }

    private fun updateUI() {
        val subtotal = CartManager.cartItems.sumOf { it.price * it.quantity }
        subtotalTxt.text = "%.2f MAD".format(subtotal)
        totalTxt.text = "%.2f MAD".format(subtotal)
    }

    private fun setupBottomMenu() {
        findViewById<View>(R.id.tabAccueilCart).setOnClickListener {
            val i = Intent(this, HomeActivity::class.java)
            i.putExtra("activeTab", "accueil")
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(i)
            finish()
        }
        findViewById<View>(R.id.tabCategorieCart).setOnClickListener {
            val i = Intent(this, HomeActivity::class.java)
            i.putExtra("activeTab", "categorie")
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(i)
            finish()
        }
        findViewById<View>(R.id.tabProfilCart).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}
