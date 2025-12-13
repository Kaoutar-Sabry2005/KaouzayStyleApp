package com.example.kaouzaystyle.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaouzaystyle.R
import com.example.kaouzaystyle.Product
import com.example.kaouzaystyle.ui.product.ProductAdapter
import com.example.kaouzaystyle.ui.panier.PanierActivity
import com.example.kaouzaystyle.ui.profile.ProfileActivity
import com.example.kaouzaystyle.ui.favorite.FavoritesActivity
import com.example.kaouzaystyle.data.remote.RetrofitClient
import com.example.kaouzaystyle.service.AutoLogoutService
import com.example.kaouzaystyle.util.NotificationHelper
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var searchBar: EditText
    private var caftans: ArrayList<Product> = ArrayList()
    private var djellabas: ArrayList<Product> = ArrayList()
    private var babouches: ArrayList<Product> = ArrayList()
    private var accessoires: ArrayList<Product> = ArrayList()
    private lateinit var recyclerProducts: RecyclerView

    // Menus
    private lateinit var menuCaftan: TextView
    private lateinit var menuDjellaba: TextView
    private lateinit var menuBabouches: TextView
    private lateinit var menuAccessoires: TextView
    private lateinit var underlineCaftan: View
    private lateinit var underlineDjellaba: View
    private lateinit var underlineBabouches: View
    private lateinit var underlineAccessoires: View

    // Tabs bas
    private lateinit var tabAccueil: View
    private lateinit var tabCategorie: View
    private lateinit var tabPanier: View
    private lateinit var tabProfil: View
    private lateinit var iconAccueil: ImageView
    private lateinit var iconCategorie: ImageView
    private lateinit var iconPanier: ImageView
    private lateinit var iconProfil: ImageView
    private lateinit var menuAccueil: TextView
    private lateinit var menuCategorie: TextView
    private lateinit var menuPanier: TextView
    private lateinit var menuProfil: TextView

    private lateinit var iconGoToFavorites: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 1. Initialiser le canal de notification
        NotificationHelper.createNotificationChannel(this)

        // 2. DEMANDER LA PERMISSION (Android 13+)
        checkNotificationPermission()

        // 3. Démarrer le service de déconnexion automatique
        startService(Intent(this, AutoLogoutService::class.java))

        // Initialiser les vues
        initViews()
        recyclerProducts.layoutManager = LinearLayoutManager(this)

        // Configurer les clics sur les boutons et menus
        setupClickListeners()

        // Récupérer les produits depuis l'API
        fetchProductsFromApi()
    }

    // Détecte toute interaction de l'utilisateur
    override fun onUserInteraction() {
        super.onUserInteraction()
        // Remet le timer du service de déconnexion à zéro
        startService(Intent(this, AutoLogoutService::class.java))
    }

    // Vérifie si l'application a la permission d'envoyer des notifications
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Demander la permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }
    }

    // Résultat de la demande de permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notifications activées !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications refusées", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Récupère les produits depuis l'API et filtre par catégorie
    private fun fetchProductsFromApi() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getProducts()
                val allProducts = response.products
                // Filtrer les produits par catégories
                caftans = ArrayList(allProducts.filter {
                    it.category.equals(
                        "caftan",
                        ignoreCase = true
                    )
                })
                djellabas = ArrayList(allProducts.filter {
                    it.category.equals(
                        "djellaba",
                        ignoreCase = true
                    )
                })
                babouches = ArrayList(allProducts.filter {
                    it.category.equals(
                        "babouche",
                        ignoreCase = true
                    )
                })
                accessoires = ArrayList(allProducts.filter {
                    it.category.contains(
                        "accessoire",
                        ignoreCase = true
                    )
                })
                handleNavigationIntent()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@HomeActivity, "Erreur API", Toast.LENGTH_SHORT).show()
                handleNavigationIntent()
            }
        }
    }

    // Gérer les intents reçus pour navigation interne
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNavigationIntent()
    }

    // Reprendre l'activité et s'assurer que le service tourne
    override fun onResume() {
        super.onResume()
        if (caftans.isNotEmpty()) handleNavigationIntent()
        startService(Intent(this, AutoLogoutService::class.java))
    }

    // Gère la navigation selon les intents (onglets et catégories)
    private fun handleNavigationIntent() {
        val intent = intent
        val activeTab = intent?.getStringExtra("activeTab") ?: "accueil"
        val openCategory = intent?.getStringExtra("openCategory") ?: "caftan"

        resetBottomBarUI() // Réinitialiser l'UI du bas

        if (activeTab.lowercase() == "categorie") {
            selectBottomTab(tabCategorie)
            when (openCategory.lowercase()) {
                "djellaba" -> {
                    activateCategory(menuDjellaba, underlineDjellaba); showProducts(djellabas)
                }

                "babouches" -> {
                    activateCategory(menuBabouches, underlineBabouches); showProducts(babouches)
                }

                "accessoires" -> {
                    activateCategory(menuAccessoires, underlineAccessoires); showProducts(
                        accessoires
                    )
                }

                else -> {
                    activateCategory(menuCaftan, underlineCaftan); showProducts(caftans)
                }
            }
        } else {
            selectBottomTab(tabAccueil)
            if (::menuDjellaba.isInitialized && menuDjellaba.currentTextColor == Color.WHITE) showProducts(
                djellabas
            )
            else if (::menuBabouches.isInitialized && menuBabouches.currentTextColor == Color.WHITE) showProducts(
                babouches
            )
            else if (::menuAccessoires.isInitialized && menuAccessoires.currentTextColor == Color.WHITE) showProducts(
                accessoires
            )
            else {
                activateCategory(menuCaftan, underlineCaftan); showProducts(caftans)
            }
        }
    }

    // Réinitialise l'UI de la barre du bas
    private fun resetBottomBarUI() {
        val defaultColor = Color.parseColor("#BAB09C")
        listOf(tabAccueil, tabCategorie, tabPanier, tabProfil).forEach { it.background = null }
        listOf(iconAccueil, iconCategorie, iconPanier, iconProfil).forEach {
            it.setColorFilter(
                defaultColor
            )
        }
        listOf(menuAccueil, menuCategorie, menuPanier, menuProfil).forEach {
            it.setTextColor(
                defaultColor
            )
        }
    }

    // Initialiser toutes les vues de l'activité
    private fun initViews() {
        recyclerProducts = findViewById(R.id.recyclerProducts)
        menuCaftan = findViewById(R.id.menuCaftan)
        menuDjellaba = findViewById(R.id.menuDjellaba)
        menuBabouches = findViewById(R.id.menuBabouches)
        menuAccessoires = findViewById(R.id.menuAccessoires)
        underlineCaftan = findViewById(R.id.underlineCaftan)
        underlineDjellaba = findViewById(R.id.underlineDjellaba)
        underlineBabouches = findViewById(R.id.underlineBabouches)
        underlineAccessoires = findViewById(R.id.underlineAccessoires)
        tabAccueil = findViewById(R.id.tabAccueil)
        tabCategorie = findViewById(R.id.tabCategorie)
        tabPanier = findViewById(R.id.tabPanier)
        tabProfil = findViewById(R.id.tabProfil)
        iconAccueil = findViewById(R.id.iconAccueil)
        iconCategorie = findViewById(R.id.iconCategorie)
        iconPanier = findViewById(R.id.iconPanier)
        iconProfil = findViewById(R.id.iconProfil)
        menuAccueil = findViewById(R.id.menuAccueil)
        menuCategorie = findViewById(R.id.menuCategorie)
        menuPanier = findViewById(R.id.menuPanier)
        menuProfil = findViewById(R.id.menuProfil)
        searchBar = findViewById(R.id.searchBar)
        try {
            iconGoToFavorites = findViewById(R.id.iconGoToFavorites)
        } catch (e: Exception) {
        } // Optionnel
    }

    // Configurer les clics sur les menus et boutons
    private fun setupClickListeners() {
        menuCaftan.setOnClickListener {
            showProducts(caftans); activateCategory(
            menuCaftan,
            underlineCaftan
        ); selectBottomTab(tabCategorie)
        }
        menuDjellaba.setOnClickListener {
            showProducts(djellabas); activateCategory(
            menuDjellaba,
            underlineDjellaba
        ); selectBottomTab(tabCategorie)
        }
        menuBabouches.setOnClickListener {
            showProducts(babouches); activateCategory(
            menuBabouches,
            underlineBabouches
        ); selectBottomTab(tabCategorie)
        }
        menuAccessoires.setOnClickListener {
            showProducts(accessoires); activateCategory(
            menuAccessoires,
            underlineAccessoires
        ); selectBottomTab(tabCategorie)
        }
        tabAccueil.setOnClickListener {
            showProducts(caftans); activateCategory(
            menuCaftan,
            underlineCaftan
        ); selectBottomTab(tabAccueil)
        }
        tabCategorie.setOnClickListener {
            selectBottomTab(tabCategorie)
            if (menuCaftan.currentTextColor != Color.WHITE && menuDjellaba.currentTextColor != Color.WHITE) {
                showProducts(caftans); activateCategory(menuCaftan, underlineCaftan)
            }
        }
        tabPanier.setOnClickListener { startActivity(Intent(this, PanierActivity::class.java)) }
        tabProfil.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }
        if (::iconGoToFavorites.isInitialized) iconGoToFavorites.setOnClickListener {
            startActivity(
                Intent(this, FavoritesActivity::class.java)
            )
        }

        // Recherche dynamique sur le champ searchBar
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Filtrer les produits selon la catégorie et la recherche
    private fun filterProducts(query: String) {
        val allProducts = when {
            menuCaftan.currentTextColor == Color.WHITE -> caftans
            menuDjellaba.currentTextColor == Color.WHITE -> djellabas
            menuBabouches.currentTextColor == Color.WHITE -> babouches
            else -> accessoires
        }
        recyclerProducts.adapter = ProductAdapter(
            this,
            ArrayList(allProducts.filter { it.name.contains(query, ignoreCase = true) })
        )
    }

    // Sélectionner un onglet du bas et mettre à jour les couleurs
    private fun selectBottomTab(selectedTab: View) {
        val defaultColor = Color.parseColor("#BAB09C")
        val selectedColor = Color.parseColor("#FAB005")
        val selectedBgColor = Color.parseColor("#35342D")
        listOf(tabAccueil, tabCategorie, tabPanier, tabProfil).forEach { it.background = null }
        listOf(iconAccueil, iconCategorie, iconPanier, iconProfil).forEach {
            it.setColorFilter(
                defaultColor
            )
        }
        listOf(menuAccueil, menuCategorie, menuPanier, menuProfil).forEach {
            it.setTextColor(
                defaultColor
            )
        }
        when (selectedTab.id) {
            R.id.tabAccueil -> {
                tabAccueil.setBackgroundColor(selectedBgColor); iconAccueil.setColorFilter(
                    selectedColor
                ); menuAccueil.setTextColor(selectedColor)
            }

            R.id.tabCategorie -> {
                tabCategorie.setBackgroundColor(selectedBgColor); iconCategorie.setColorFilter(
                    selectedColor
                ); menuCategorie.setTextColor(selectedColor)
            }
        }
    }

    // Afficher les produits dans le RecyclerView
    private fun showProducts(list: ArrayList<Product>) {
        recyclerProducts.adapter = ProductAdapter(this, list)
    }

    // Activer visuellement une catégorie
    private fun activateCategory(selected: TextView, underline: View) {
        resetCategoryStyles()
        selected.setTextColor(Color.WHITE); selected.setTypeface(
            null,
            Typeface.BOLD
        ); underline.setBackgroundColor(Color.WHITE)
    }

    // Réinitialiser les styles des catégories
    private fun resetCategoryStyles() {
        val gray = Color.parseColor("#D5D0C8")
        val underlineGray = Color.parseColor("#AFAAA2")
        listOf(menuCaftan, menuDjellaba, menuBabouches, menuAccessoires).forEach {
            it.setTextColor(
                gray
            ); it.setTypeface(null, Typeface.NORMAL)
        }
        listOf(
            underlineCaftan,
            underlineDjellaba,
            underlineBabouches,
            underlineAccessoires
        ).forEach { it.setBackgroundColor(underlineGray) }
    }
}