package com.example.kaouzaystyle.ui.panier

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        val db = AppDatabase.getInstance(this)
        val repository = CartRepository(db)
        // --- MODIF : Passage de 'application' ---
        val factory = CartViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, factory)[CartViewModel::class.java]
        // ----------------------------------------

        val recyclerView = findViewById<RecyclerView>(R.id.cartRecyclerView)
        subtotalTxt = findViewById(R.id.subtotalAmount)
        totalTxt = findViewById(R.id.totalAmount)
        val btnCheckout = findViewById<Button>(R.id.checkoutButton)
        val btnBack = findViewById<ImageView>(R.id.cartBackButton)
        btnClearCart = findViewById(R.id.clearCartButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CartAdapter(emptyList()) { item ->
            viewModel.addToCart(item)
        }
        recyclerView.adapter = adapter

        viewModel.cartItems.observe(this) { items ->
            adapter.submitList(items)
            updateUI(items)

            btnClearCart.isEnabled = items.isNotEmpty()
            if (items.isEmpty()) {
                btnClearCart.alpha = 0.3f
            } else {
                btnClearCart.alpha = 1.0f
            }
        }

        btnClearCart.setOnClickListener {
            if (adapter.itemCount > 0) {
                afficherConfirmationViderPanier()
            } else {
                Toast.makeText(this, "Le panier est déjà vide", Toast.LENGTH_SHORT).show()
            }
        }

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