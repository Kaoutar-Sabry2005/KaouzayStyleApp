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

// Activité pour gérer et afficher le panier
class PanierActivity : AppCompatActivity() {

    private lateinit var subtotalTxt: TextView // Montant total partiel
    private lateinit var totalTxt: TextView // Montant total
    private lateinit var adapter: CartAdapter // Adapter du RecyclerView
    private lateinit var viewModel: CartViewModel // ViewModel pour le panier
    private lateinit var btnClearCart: ImageView // Bouton pour vider le panier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialiser la base de données et le repository
        val db = AppDatabase.getInstance(this)
        val repository = CartRepository(db)

        // Créer le ViewModel avec la factory
        val factory = CartViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, factory)[CartViewModel::class.java]

        // Initialiser les vues
        val recyclerView = findViewById<RecyclerView>(R.id.cartRecyclerView)
        subtotalTxt = findViewById(R.id.subtotalAmount)
        totalTxt = findViewById(R.id.totalAmount)
        val btnCheckout = findViewById<Button>(R.id.checkoutButton)
        val btnBack = findViewById<ImageView>(R.id.cartBackButton)
        btnClearCart = findViewById(R.id.clearCartButton)

        recyclerView.layoutManager = LinearLayoutManager(this) // Layout vertical

        // Configurer l'adapter du panier
        adapter = CartAdapter(emptyList()) { item ->
            viewModel.addToCart(item) // Mettre à jour la quantité dans le ViewModel
        }
        recyclerView.adapter = adapter

        // Observer les changements dans le panier
        viewModel.cartItems.observe(this) { items ->
            adapter.submitList(items) // Mettre à jour l'affichage
            updateUI(items) // Mettre à jour les montants

            // Activer ou désactiver le bouton vider panier
            btnClearCart.isEnabled = items.isNotEmpty()
            btnClearCart.alpha = if (items.isEmpty()) 0.3f else 1.0f
        }

        // Action pour vider le panier
        btnClearCart.setOnClickListener {
            if (adapter.itemCount > 0) {
                afficherConfirmationViderPanier()
            } else {
                Toast.makeText(this, "Le panier est déjà vide", Toast.LENGTH_SHORT).show()
            }
        }

        // Action pour passer au paiement
        btnCheckout.setOnClickListener {
            if (adapter.itemCount == 0) {
                Toast.makeText(this, "Le panier est vide", Toast.LENGTH_SHORT).show()
            } else {
                val i = Intent(this, PaymentActivity::class.java)
                i.putExtra("TOTAL_AMOUNT", totalTxt.text.toString())
                startActivity(i)
            }
        }

        // Bouton retour
        btnBack.setOnClickListener { finish() }

        // Configurer le menu du bas
        setupBottomMenu()
    }

    // Afficher une boîte de dialogue pour confirmer le vidage du panier
    private fun afficherConfirmationViderPanier() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Vider le panier ?")
        builder.setMessage("Êtes-vous sûr de vouloir supprimer tous les articles ?")

        builder.setPositiveButton("Oui") { _, _ ->
            viewModel.clearCart() // Vider le panier
            Toast.makeText(this, "Panier vidé", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Non") { dialog, _ ->
            dialog.dismiss() // Fermer la boîte de dialogue
        }

        val dialog = builder.create()
        dialog.show()
    }

    // Met à jour les montants du panier
    private fun updateUI(items: List<ProductCart>) {
        val subtotal = items.sumOf { it.price * it.quantity }
        subtotalTxt.text = "%.2f MAD".format(subtotal)
        totalTxt.text = "%.2f MAD".format(subtotal)
    }

    // Configurer les clics pour naviguer avec le menu du bas
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
