package com.example.kaouzaystyle.ui.panier

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog // Pour la fenêtre de confirmation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaouzaystyle.R
import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.data.local.entity.ProductCart
import com.example.kaouzaystyle.data.repository.CartRepository
import com.example.kaouzaystyle.ui.home.HomeActivity
import com.example.kaouzaystyle.ui.profile.ProfileActivity

class PanierActivity : AppCompatActivity() {

    private lateinit var subtotalTxt: TextView
    private lateinit var totalTxt: TextView
    private lateinit var adapter: CartAdapter
    private lateinit var viewModel: CartViewModel

    private lateinit var btnClearCart: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        //  1. Initialisation ViewModel
        val db = AppDatabase.getInstance(this)
        val repository = CartRepository(db)
        val factory = CartViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CartViewModel::class.java]

        //  2. Initialisation des Vues
        val recyclerView = findViewById<RecyclerView>(R.id.cartRecyclerView)
        subtotalTxt = findViewById(R.id.subtotalAmount)
        totalTxt = findViewById(R.id.totalAmount)
        val btnCheckout = findViewById<Button>(R.id.checkoutButton)
        val btnBack = findViewById<ImageView>(R.id.cartBackButton)

        btnClearCart = findViewById(R.id.clearCartButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        //  3. Initialisation Adapter
        adapter = CartAdapter(emptyList()) { item ->
            viewModel.addToCart(item)
        }
        recyclerView.adapter = adapter

        //  4. Observer les données
        viewModel.cartItems.observe(this) { items ->
            adapter.submitList(items)
            updateUI(items)

            // Gestion de l'état du bouton (activé/désactivé + transparence)
            btnClearCart.isEnabled = items.isNotEmpty()
            if (items.isEmpty()) {
                btnClearCart.alpha = 0.3f // Très transparent si vide
            } else {
                btnClearCart.alpha = 1.0f // Pleinement visible si plein
            }
        }

        //  5. Action du bouton "Vider le panier"
        btnClearCart.setOnClickListener {
            if (adapter.itemCount > 0) {
                afficherConfirmationViderPanier()
            } else {
                Toast.makeText(this, "Le panier est déjà vide", Toast.LENGTH_SHORT).show()
            }
        }

        // Action Paiement
        btnCheckout.setOnClickListener {
            if (adapter.itemCount == 0) {
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

    // --- Fonction pour afficher la boîte de dialogue ---
    private fun afficherConfirmationViderPanier() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Vider le panier ?")
        builder.setMessage("Êtes-vous sûr de vouloir supprimer tous les articles ?")

        builder.setPositiveButton("Oui") { _, _ ->
            viewModel.clearCart()
            Toast.makeText(this, "Panier vidé", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Non") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun updateUI(items: List<ProductCart>) {
        val subtotal = items.sumOf { it.price * it.quantity }
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



