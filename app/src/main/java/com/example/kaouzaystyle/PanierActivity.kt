package com.example.kaouzaystyle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PanierActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var subtotalAmount: TextView
    private lateinit var totalAmount: TextView
    private lateinit var checkoutButton: Button

    private val shipping = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Recycler et Totaux
        recyclerView = findViewById(R.id.cartRecyclerView)
        subtotalAmount = findViewById(R.id.subtotalAmount)
        totalAmount = findViewById(R.id.totalAmount)
        checkoutButton = findViewById(R.id.checkoutButton)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CartAdapter(CartManager.cartItems) {
            updateTotals()
        }
        updateTotals()

        checkoutButton.setOnClickListener {
            // Crée un intent vers l'activité de paiement
            val intent = Intent(this, PaymentActivity::class.java)

            // Tu peux passer le total à PaymentActivity si tu veux
            intent.putExtra("TOTAL_AMOUNT", totalAmount.text.toString())

            // Démarre l'activité de paiement
            startActivity(intent)
        }


        // Menu bas
        setupBottomMenu()

        // Flèche retour : revient à HomeActivity sur la catégorie précédente
        findViewById<ImageView>(R.id.cartBackButton).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("activeTab", "accueil") // ou "categorie" selon le cas
            intent.putExtra("openCategory", "caftan") // ou la dernière catégorie choisie
            startActivity(intent)
            finish()
        }


    }

    private fun updateTotals() {
        val subtotal = CartManager.cartItems.sumOf { it.price * it.quantity }
        val total = subtotal + shipping
        subtotalAmount.text = "%.2f MAD".format(subtotal)
        totalAmount.text = "%.2f MAD".format(total)
    }

    // --- MENU BAS ---
    private fun setupBottomMenu() {
        // Accueil
        // Accueil
        findViewById<ImageView>(R.id.iconAccueilCart).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("activeTab", "accueil")
            startActivity(intent)
            finish()
        }

        findViewById<ImageView>(R.id.iconCategorieCart).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("activeTab", "categorie")
            intent.putExtra("openCategory", "caftan")
            startActivity(intent)
            finish()
        }



        // Profil
        findViewById<ImageView>(R.id.iconProfilCart).setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
            finish()
        }
    }

    // --- NAVIGATION VERS HOME ---
    private fun navigateToHomeWithCategory() {
        // On récupère la dernière catégorie choisie si on en a besoin
        val lastCategory = intent.getStringExtra("lastCategory") ?: "caftan"

        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra("fromCart", true)
        intent.putExtra("openCategory", lastCategory) // ouvre la catégorie précédemment sélectionnée
        startActivity(intent)
        finish()
    }
}
