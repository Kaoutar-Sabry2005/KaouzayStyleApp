package com.example.kaouzaystyle.ui.home

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // Pour lancer le téléchargement
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaouzaystyle.R
import com.example.kaouzaystyle.Product
import com.example.kaouzaystyle.ui.product.ProductAdapter
import com.example.kaouzaystyle.ui.panier.PanierActivity
import com.example.kaouzaystyle.ui.profile.ProfileActivity
import com.example.kaouzaystyle.data.remote.RetrofitClient // Importez votre client API
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var searchBar: EditText

    private var caftans: ArrayList<Product> = ArrayList()
    private var djellabas: ArrayList<Product> = ArrayList()
    private var babouches: ArrayList<Product> = ArrayList()
    private var accessoires: ArrayList<Product> = ArrayList()

    private lateinit var recyclerProducts: RecyclerView

    // Menu haut
    private lateinit var menuCaftan: TextView
    private lateinit var menuDjellaba: TextView
    private lateinit var menuBabouches: TextView
    private lateinit var menuAccessoires: TextView
    private lateinit var underlineCaftan: View
    private lateinit var underlineDjellaba: View
    private lateinit var underlineBabouches: View
    private lateinit var underlineAccessoires: View

    // Menu bas
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initViews()
        recyclerProducts.layoutManager = LinearLayoutManager(this)

        setupClickListeners()

        fetchProductsFromApi()
    }

    // Fonction pour télécharger le JSON
    private fun fetchProductsFromApi() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getProducts()
                val allProducts = response.products // La liste complète

                caftans = ArrayList(allProducts.filter { it.category.equals("caftan", ignoreCase = true) })
                djellabas = ArrayList(allProducts.filter { it.category.equals("djellaba", ignoreCase = true) })
                babouches = ArrayList(allProducts.filter { it.category.equals("babouche", ignoreCase = true) })

                // Attention : dans votre JSON c'est "accessoires" (pluriel)
                accessoires = ArrayList(allProducts.filter { it.category.contains("accessoire", ignoreCase = true) })

                handleNavigationIntent()

                android.util.Log.d("API_SUCCESS", "Reçu ${allProducts.size} produits au total")

            } catch (e: Exception) {
                e.printStackTrace()
                android.util.Log.e("API_ERROR", "Erreur: ${e.message}")
                Toast.makeText(this@HomeActivity, "Erreur chargement: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNavigationIntent()
    }

    override fun onResume() {
        super.onResume()
        handleNavigationIntent()
    }

    private fun handleNavigationIntent() {
        val intent = intent
        val activeTab = intent?.getStringExtra("activeTab") ?: "accueil"
        val openCategory = intent?.getStringExtra("openCategory") ?: "caftan"

        resetBottomBarUI()

        if (activeTab.lowercase() == "categorie") {
            selectBottomTab(tabCategorie)
            when (openCategory.lowercase()) {
                "djellaba" -> { activateCategory(menuDjellaba, underlineDjellaba); showProducts(djellabas) }
                "babouches" -> { activateCategory(menuBabouches, underlineBabouches); showProducts(babouches) }
                "accessoires" -> { activateCategory(menuAccessoires, underlineAccessoires); showProducts(accessoires) }
                else -> { activateCategory(menuCaftan, underlineCaftan); showProducts(caftans) }
            }
        } else {
            selectBottomTab(tabAccueil)
            if (::menuDjellaba.isInitialized && menuDjellaba.currentTextColor == Color.WHITE) showProducts(djellabas)
            else if (::menuBabouches.isInitialized && menuBabouches.currentTextColor == Color.WHITE) showProducts(babouches)
            else if (::menuAccessoires.isInitialized && menuAccessoires.currentTextColor == Color.WHITE) showProducts(accessoires)
            else { activateCategory(menuCaftan, underlineCaftan); showProducts(caftans) }
        }
    }

    private fun resetBottomBarUI() {
        val defaultColor = Color.parseColor("#BAB09C")
        listOf(tabAccueil, tabCategorie, tabPanier, tabProfil).forEach { it.background = null }
        listOf(iconAccueil, iconCategorie, iconPanier, iconProfil).forEach { it.setColorFilter(defaultColor) }
        listOf(menuAccueil, menuCategorie, menuPanier, menuProfil).forEach { it.setTextColor(defaultColor) }
    }

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
    }

    private fun setupClickListeners() {
        menuCaftan.setOnClickListener {
            showProducts(caftans)
            activateCategory(menuCaftan, underlineCaftan)
            selectBottomTab(tabCategorie)
        }
        menuDjellaba.setOnClickListener {
            showProducts(djellabas)
            activateCategory(menuDjellaba, underlineDjellaba)
            selectBottomTab(tabCategorie)
        }
        menuBabouches.setOnClickListener {
            showProducts(babouches)
            activateCategory(menuBabouches, underlineBabouches)
            selectBottomTab(tabCategorie)
        }
        menuAccessoires.setOnClickListener {
            showProducts(accessoires)
            activateCategory(menuAccessoires, underlineAccessoires)
            selectBottomTab(tabCategorie)
        }

        tabAccueil.setOnClickListener {
            showProducts(caftans)
            activateCategory(menuCaftan, underlineCaftan)
            selectBottomTab(tabAccueil)
        }

        tabCategorie.setOnClickListener {
            selectBottomTab(tabCategorie)
            if (menuCaftan.currentTextColor != Color.WHITE &&
                menuDjellaba.currentTextColor != Color.WHITE &&
                menuBabouches.currentTextColor != Color.WHITE &&
                menuAccessoires.currentTextColor != Color.WHITE) {
                showProducts(caftans)
                activateCategory(menuCaftan, underlineCaftan)
            }
        }

        tabPanier.setOnClickListener {
            startActivity(Intent(this, PanierActivity::class.java))
        }

        tabProfil.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProducts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterProducts(query: String) {
        val allProducts = when {
            menuCaftan.currentTextColor == Color.WHITE -> caftans
            menuDjellaba.currentTextColor == Color.WHITE -> djellabas
            menuBabouches.currentTextColor == Color.WHITE -> babouches
            menuAccessoires.currentTextColor == Color.WHITE -> accessoires
            else -> caftans
        }
        val filteredList = allProducts.filter {
            it.name.contains(query, ignoreCase = true)
        }
        recyclerProducts.adapter = ProductAdapter(this, ArrayList(filteredList))
    }

    private fun selectBottomTab(selectedTab: View) {
        val defaultColor = Color.parseColor("#BAB09C")
        val selectedColor = Color.parseColor("#FAB005")
        val selectedBgColor = Color.parseColor("#35342D")

        listOf(tabAccueil, tabCategorie, tabPanier, tabProfil).forEach { it.background = null }
        listOf(iconAccueil, iconCategorie, iconPanier, iconProfil).forEach { it.setColorFilter(defaultColor) }
        listOf(menuAccueil, menuCategorie, menuPanier, menuProfil).forEach { it.setTextColor(defaultColor) }

        when (selectedTab.id) {
            R.id.tabAccueil -> {
                tabAccueil.setBackgroundColor(selectedBgColor)
                iconAccueil.setColorFilter(selectedColor)
                menuAccueil.setTextColor(selectedColor)
            }
            R.id.tabCategorie -> {
                tabCategorie.setBackgroundColor(selectedBgColor)
                iconCategorie.setColorFilter(selectedColor)
                menuCategorie.setTextColor(selectedColor)
            }
        }
    }

    private fun showProducts(list: ArrayList<Product>) {
        recyclerProducts.adapter = ProductAdapter(this, list)
    }

    private fun activateCategory(selected: TextView, underline: View) {
        resetCategoryStyles()
        selected.setTextColor(Color.WHITE)
        selected.setTypeface(null, Typeface.BOLD)
        underline.setBackgroundColor(Color.WHITE)
    }

    private fun resetCategoryStyles() {
        val gray = Color.parseColor("#D5D0C8")
        val underlineGray = Color.parseColor("#AFAAA2")
        listOf(menuCaftan, menuDjellaba, menuBabouches, menuAccessoires).forEach {
            it.setTextColor(gray)
            it.setTypeface(null, Typeface.NORMAL)
        }
        listOf(underlineCaftan, underlineDjellaba, underlineBabouches, underlineAccessoires).forEach {
            it.setBackgroundColor(underlineGray)
        }
    }

    // La fonction loadProducts() hardcodée a été supprimée et remplacée par fetchProductsFromApi()
}
