package com.example.kaouzaystyle

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    private lateinit var caftans: ArrayList<Product>
    private lateinit var djellabas: ArrayList<Product>
    private lateinit var babouches: ArrayList<Product>
    private lateinit var accessoires: ArrayList<Product>

    private lateinit var recyclerProducts: RecyclerView

    // Catégories (haut)
    private lateinit var menuCaftan: TextView
    private lateinit var menuDjellaba: TextView
    private lateinit var menuBabouches: TextView
    private lateinit var menuAccessoires: TextView

    private lateinit var underlineCaftan: View
    private lateinit var underlineDjellaba: View
    private lateinit var underlineBabouches: View
    private lateinit var underlineAccessoires: View

    // Menu bas
    private lateinit var menuAccueil: TextView
    private lateinit var menuCategorie: TextView
    private lateinit var menuPanier: TextView
    private lateinit var menuProfil: TextView

    private lateinit var iconAccueil: ImageView
    private lateinit var iconCategorie: ImageView
    private lateinit var iconProfil: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerProducts = findViewById(R.id.recyclerProducts)
        recyclerProducts.layoutManager = LinearLayoutManager(this)

        loadProducts()

        // Catégories haut
        menuCaftan = findViewById(R.id.menuCaftan)
        menuDjellaba = findViewById(R.id.menuDjellaba)
        menuBabouches = findViewById(R.id.menuBabouches)
        menuAccessoires = findViewById(R.id.menuAccessoires)

        underlineCaftan = findViewById(R.id.underlineCaftan)
        underlineDjellaba = findViewById(R.id.underlineDjellaba)
        underlineBabouches = findViewById(R.id.underlineBabouches)
        underlineAccessoires = findViewById(R.id.underlineAccessoires)

        // Menu bas
        menuAccueil = findViewById(R.id.menuAccueil)
        menuCategorie = findViewById(R.id.menuCategorie)
        menuPanier = findViewById(R.id.menuPanier)
        menuProfil = findViewById(R.id.menuProfil)

        iconAccueil = findViewById(R.id.iconAccueil)
        iconCategorie = findViewById(R.id.iconCategorie)
        iconProfil = findViewById(R.id.iconProfil)

        // Écran d’accueil : Caftan + Accueil blanc
        showProducts(caftans)
        activateCategory(menuCaftan, underlineCaftan)
        activateBottom(menuAccueil, iconAccueil)

        // Click catégories haut
        menuCaftan.setOnClickListener {
            showProducts(caftans)
            activateCategory(menuCaftan, underlineCaftan)
            activateBottom(menuAccueil, iconAccueil) // Accueil sélectionné
        }

        menuDjellaba.setOnClickListener {
            showProducts(djellabas)
            activateCategory(menuDjellaba, underlineDjellaba)
            activateBottom(menuCategorie, iconCategorie) // Catégorie sélectionné
        }

        menuBabouches.setOnClickListener {
            showProducts(babouches)
            activateCategory(menuBabouches, underlineBabouches)
            activateBottom(menuCategorie, iconCategorie) // Catégorie sélectionné
        }

        menuAccessoires.setOnClickListener {
            showProducts(accessoires)
            activateCategory(menuAccessoires, underlineAccessoires)
            activateBottom(menuCategorie, iconCategorie) // Catégorie sélectionné
        }

        // Click Accueil → retour Caftan
        menuAccueil.setOnClickListener {
            showProducts(caftans)
            activateCategory(menuCaftan, underlineCaftan)
            activateBottom(menuAccueil, iconAccueil)
        }

        // Click Catégorie bas
        menuCategorie.setOnClickListener {
            activateBottom(menuCategorie, iconCategorie)
        }
    }

    private fun showProducts(list: ArrayList<Product>) {
        recyclerProducts.adapter = ProductAdapter(this, list)
    }

    private fun loadProducts() {
        caftans = arrayListOf(
            Product("Takchita Traditionnelle Moderne – Gris & Bordeaux", "6500.00 MAD", R.drawable.caftan1),
            Product("Caftan Tarz Lfassi Royale – Blanc Pur & Détails Bleus", "7200.00 MAD", R.drawable.caftan2),
            Product("Caftan Royal Violet Métallisé – Collection Prestige", "5800.00 MAD", R.drawable.caftan3),
            Product("Takchita Classique en Bleu Royal", "8000.00 MAD", R.drawable.caftan4),
            Product("Caftan Haute Couture Argenté – Ceinture Saphir et Tissu Jaouhara", "6900.00 MAD", R.drawable.caftan5)
        )

        djellabas = arrayListOf(
            Product("Djellaba Classy avec Détails Traditionnels Noirs", "450.00 MAD", R.drawable.djellaba1),
            Product("Djellaba Raffinée en Ton Sable avec Sfifa Grise", "520.00 MAD", R.drawable.djellaba2),
            Product("Djellaba Luxe Satinée Vert d’Eau – Broderies Raffinées", "600.00 MAD", R.drawable.djellaba3),
            Product("Djellaba Haute Élégance Chocolat – Style Moderne", "750.00 MAD", R.drawable.djellaba4),
            Product("Djellaba Élégance Bleu Gris – Finitions Maalem", "680.00 MAD", R.drawable.djellaba5)
        )

        babouches = arrayListOf(
            Product("Babouches Traditionnelles Vert Royal – Broderie Luxe", "300.00 MAD", R.drawable.babouche1),
            Product("Mules Élégantes Beige avec Perles Dorées", "450.00 MAD", R.drawable.babouche2),
            Product("Babouches Berbères Artisanales – Bleu Nuit & Motifs Colorés", "200.00 MAD", R.drawable.babouche3),
            Product("Babouches Authentiques Amazigh – Fabrication Traditionnelle", "400.00 MAD", R.drawable.babouche4),
            Product("Babouches Idokan – Artisanat Marocain Traditionnel", "90.00 MAD", R.drawable.babouche5)
        )

        accessoires = arrayListOf(
            Product("Ensemble Bijoux Artisanaux en Or et Pierres Naturelles", "1000.00 MAD", R.drawable.accessoire1),
            Product("Joher Prestige – Perles Raffinées & Or Filigrané", "800.00 MAD", R.drawable.accessoire2),
            Product("Boucles d’Oreilles Luxe – Filigrane Doré & Cristal Bleu", "250.00 MAD", R.drawable.accessoire3),
            Product("Set de Bracelets Dorés – Arabesques Luxueuses", "5600.00 MAD", R.drawable.accessoire4),
            Product("Colliers Amazighes “Tazerzit” en Argent et Pierres", "600.00 MAD", R.drawable.accessoire5)
        )
    }

    private fun resetCategoryStyles() {
        val gray = Color.parseColor("#D5D0C8")
        val underlineGray = Color.parseColor("#AFAAA2")

        menuCaftan.setTextColor(gray)
        underlineCaftan.setBackgroundColor(underlineGray)

        menuDjellaba.setTextColor(gray)
        underlineDjellaba.setBackgroundColor(underlineGray)

        menuBabouches.setTextColor(gray)
        underlineBabouches.setBackgroundColor(underlineGray)

        menuAccessoires.setTextColor(gray)
        underlineAccessoires.setBackgroundColor(underlineGray)

        menuCaftan.setTypeface(null, Typeface.NORMAL)
        menuDjellaba.setTypeface(null, Typeface.NORMAL)
        menuBabouches.setTypeface(null, Typeface.NORMAL)
        menuAccessoires.setTypeface(null, Typeface.NORMAL)
    }

    private fun activateCategory(selected: TextView, underline: View) {
        resetCategoryStyles()
        selected.setTextColor(Color.WHITE)
        selected.setTypeface(null, Typeface.BOLD)
        underline.setBackgroundColor(Color.WHITE)
    }

    private fun activateBottom(selected: TextView, icon: ImageView) {

        val gray = Color.parseColor("#AAAAAA")
        val dark = Color.parseColor("#181611")

        // Reset tout
        menuAccueil.setTextColor(gray)
        menuCategorie.setTextColor(gray)
        menuPanier.setTextColor(gray)
        menuProfil.setTextColor(gray)

        iconAccueil.setColorFilter(gray)
        iconCategorie.setColorFilter(gray)
        iconProfil.setColorFilter(gray)

        menuAccueil.setBackgroundColor(dark)
        menuCategorie.setBackgroundColor(dark)
        menuPanier.setBackgroundColor(dark)
        menuProfil.setBackgroundColor(dark)

        // Icône + texte blancs
        selected.setTextColor(Color.BLACK)
        icon.setColorFilter(Color.BLACK)
        selected.setBackgroundColor(Color.WHITE)
    }
}
