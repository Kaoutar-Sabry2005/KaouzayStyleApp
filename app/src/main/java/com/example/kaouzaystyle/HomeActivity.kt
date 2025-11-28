package com.example.kaouzaystyle

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


class HomeActivity : AppCompatActivity() {
    private lateinit var searchBar: EditText

    private lateinit var caftans: ArrayList<Product>
    private lateinit var djellabas: ArrayList<Product>
    private lateinit var babouches: ArrayList<Product>
    private lateinit var accessoires: ArrayList<Product>

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
        loadProducts()
        recyclerProducts.layoutManager = LinearLayoutManager(this)
        setupClickListeners()

        // Valeurs par défaut
        var defaultCategory = "caftan"
        var defaultTab = "accueil"

        // Lire les extras depuis l'intent
        val fromCart = intent.getBooleanExtra("fromCart", false)
        val activeTab = intent.getStringExtra("activeTab") ?: if (fromCart) "categorie" else "accueil"
        val openCategory = intent.getStringExtra("openCategory") ?: defaultCategory

        // Réinitialiser l'UI du menu bas
        // Réinitialiser menu bas
        resetBottomBarUI()

        if (activeTab.lowercase() == "categorie") {
            when (openCategory.lowercase()) {
                "caftan" -> { activateCategory(menuCaftan, underlineCaftan); showProducts(caftans) }
                "djellaba" -> { activateCategory(menuDjellaba, underlineDjellaba); showProducts(djellabas) }
                "babouches" -> { activateCategory(menuBabouches, underlineBabouches); showProducts(babouches) }
                "accessoires" -> { activateCategory(menuAccessoires, underlineAccessoires); showProducts(accessoires) }
                else -> { activateCategory(menuCaftan, underlineCaftan); showProducts(caftans) }
            }
        } else {
            // Pour Accueil ou autre onglet
            showProducts(caftans)
            activateCategory(menuCaftan, underlineCaftan)
        }

// Sélectionner le tab bas
        when (activeTab.lowercase()) {
            "accueil" -> selectBottomTab(tabAccueil)
            "categorie" -> selectBottomTab(tabCategorie)
            "panier" -> selectBottomTab(tabPanier)
            "profil" -> selectBottomTab(tabProfil)
            else -> selectBottomTab(tabAccueil)
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

        //la barre de rechreche
        searchBar = findViewById(R.id.searchBar)

    }

    private fun setupClickListeners() {
        // Catégories du haut
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

        // Menu bas
        tabAccueil.setOnClickListener {
            showProducts(caftans)
            activateCategory(menuCaftan, underlineCaftan)
            selectBottomTab(tabAccueil)
        }
        tabCategorie.setOnClickListener {
            selectBottomTab(tabCategorie)
            showProducts(caftans)              // Affiche une catégorie par défaut
            activateCategory(menuCaftan, underlineCaftan)
        }

        // Onglet Panier
        tabPanier.setOnClickListener {
            selectBottomTab(tabPanier)
            startActivity(Intent(this, PanierActivity::class.java))
        }
        tabProfil.setOnClickListener {
            selectBottomTab(tabProfil)
            startActivity(Intent(this, ProfilActivity::class.java))
        }
        //la barre de rechrech
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

        // Reset
        listOf(tabAccueil, tabCategorie, tabPanier, tabProfil).forEach { it.background = null }
        listOf(iconAccueil, iconCategorie, iconPanier, iconProfil).forEach { it.setColorFilter(defaultColor) }
        listOf(menuAccueil, menuCategorie, menuPanier, menuProfil).forEach { it.setTextColor(defaultColor) }

        // Appliquer style sélectionné
        when (selectedTab.id) {
            R.id.tabAccueil -> { tabAccueil.setBackgroundColor(selectedBgColor); iconAccueil.setColorFilter(selectedColor); menuAccueil.setTextColor(selectedColor) }
            R.id.tabCategorie -> { tabCategorie.setBackgroundColor(selectedBgColor); iconCategorie.setColorFilter(selectedColor); menuCategorie.setTextColor(selectedColor) }
            R.id.tabPanier -> { tabPanier.setBackgroundColor(selectedBgColor); iconPanier.setColorFilter(selectedColor); menuPanier.setTextColor(selectedColor) }
            R.id.tabProfil -> { tabProfil.setBackgroundColor(selectedBgColor); iconProfil.setColorFilter(selectedColor); menuProfil.setTextColor(selectedColor) }
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
        listOf(menuCaftan, menuDjellaba, menuBabouches, menuAccessoires).forEach { it.setTextColor(gray); it.setTypeface(null, Typeface.NORMAL) }
        listOf(underlineCaftan, underlineDjellaba, underlineBabouches, underlineAccessoires).forEach { it.setBackgroundColor(underlineGray) }
    }

    // === Chargement des produits (inchangé) ===
    private fun loadProducts() {
        // --- COULEURS DE BASE ---
        val colorGreen = Color.parseColor("#006400")
        val colorGold = Color.parseColor("#FAB005")
        val colorBlue = Color.parseColor("#000080")
        val colorRed = Color.parseColor("#8B0000")
        val colorWhite = Color.parseColor("#FFFFFF")
        val colorBlack = Color.parseColor("#000000")

        // Couleurs spécifiques (extraites de votre logique précédente)
        val colorFonce = Color.parseColor("#0D1C07")
        val colorBordeaux = Color.parseColor("#340707")

        // --- TAILLES ---
        val sizesVetements = listOf("S", "M", "L", "XL", "2XL")
        val sizesChaussures = listOf("36", "37", "38", "39", "40")
        val sizeUnique = listOf("Unique")

        // ---------------- CAFTANS ----------------
        caftans = arrayListOf(
            Product(
                "Takchita Tarz Fassi",
                "7200.00 MAD",
                R.drawable.takchitavert,
                "Découvrez l'élégance pure avec notre Takchita Tarz Fassi. Fabriquée à la main en tissu blanc fluide, elle est ornée de broderies Tarz Fassi traditionnelles. Un chef-d'œuvre de l'artisanat marocain, parfait pour vos célébrations.",
                listOf(
                    ProductVariant(colorFonce, R.drawable.takchitavert),
                    ProductVariant(colorBordeaux, R.drawable.takchitarouge)
                ),
                listOf("M", "L", "XL")
            ),
            Product(
                "Caftan en Satin",
                "6500.00 MAD",
                R.drawable.caftanvert,
                "Caftan moderne et élégant en satin de soie vert olive ou bleu, rehaussé d'une broderie ton sur ton et ceinturé par une large ceinture du même tissu.",
                listOf(
                    ProductVariant(colorGreen, R.drawable.caftanvert),
                    ProductVariant(colorBlue, R.drawable.caftanbleu)
                ),
                sizesVetements
            ),
            Product(
                "Caftan Jawhara Royal",
                "5800.00 MAD",
                R.drawable.caftanjawharawhite,
                "Caftan de mariage ou de cérémonie en tissu Jawhara (souvent rayé et texturé).",
                listOf(
                    ProductVariant(colorWhite, R.drawable.caftanjawharawhite),
                    // Correction ICI : Color.parseColor(...)
                    ProductVariant(Color.parseColor("#FF5E00"), R.drawable.caftanjawharaorange),
                    ProductVariant(Color.parseColor("#4E4AA4"), R.drawable.caftanjawharableu)
                ),
                sizesVetements
            ),
            Product(
                "Caftan Haute Couture Argenté",
                "6900.00 MAD",
                R.drawable.caftanbleuu,
                "Caftan majestueux en tissu brocard argenté, souligné par des bordures et une ceinture en velours bleu ou vert royal pour un contraste riche.",
                listOf(
                    ProductVariant(colorBlue, R.drawable.caftanbleuu),
                    ProductVariant(colorFonce, R.drawable.caftangreen)
                ),
                sizesVetements
            ),
            Product(
                "Tenue Traditionnelle Soussia/Amazighe",
                "8000.00 MAD",
                R.drawable.caftansoussijaune,
                "Tenue régionale du Sud (Souss) caractérisée par ses couluer, des broderies vives et une abondance de bijoux traditionnels en argent.",
                listOf(
                    ProductVariant(colorGold, R.drawable.caftansoussijaune),
                    ProductVariant(colorRed, R.drawable.caftansoussirouge),
                    ProductVariant(Color.parseColor("#FF6F00"), R.drawable.caftansoussiorange)
                ),
                sizesVetements
            )
        )

        // ---------------- DJELLABAS ----------------
        djellabas = arrayListOf(
            Product(
                "Djellaba Classy",
                "450.00 MAD",
                R.drawable.djellabavert,
                "Djellaba luxueuse en tissu brocard (jacquard de soie) avec des motifs lilas/mauves, souvent portée par-dessus un kmiss (sous-vêtement).",
                listOf(
                    ProductVariant(Color.parseColor("#659797"), R.drawable.djellabavert),
                    ProductVariant(Color.parseColor("#990D6F"), R.drawable.djellabaviolet),
                    ProductVariant(Color.parseColor("#C5C73D"), R.drawable.djellabajaune)
                ),
                sizesVetements
            ),
            Product(
                "Djellaba Élégance",
                "680.00 MAD",
                R.drawable.djellabaorange,
                "Djellaba à capuche avec de riches broderies ton sur ton et des motifs blancs ou écrus sur le col et les poignets.",
                listOf(
                    ProductVariant(Color.parseColor("#F84C08"), R.drawable.djellabaorange),
                    ProductVariant(Color.parseColor("#B68977"), R.drawable.djellababeige)
                ),
                sizesVetements
            ),
            Product(
                "Djellaba Grise Claire à Sfifa",
                "520.00 MAD",
                R.drawable.djellaba2,
                "Djellaba gris clair/beige au style minimaliste, soulignée par une sfifa foncée gris anthracite pour définir la coupe.",
                listOf(
                    ProductVariant(Color.parseColor("#B2B5BA"), R.drawable.djellaba2)
                ),
                sizesVetements
            ),
            Product(
                "Djellaba Bleu Pétrole à Capuche",
                "600.00 MAD",
                R.drawable.djellaba5,
                "Djellaba fluide bleu pétrole ou bleu céladon, avec des détails de sfifa assortie ou argentée et une coupe droite.",
                listOf(
                    ProductVariant(Color.parseColor("#6A87AD"), R.drawable.djellaba5)
                ),
                listOf("S", "M", "L")
            ),
            Product(
                "Djellaba Haute Élégance",
                "750.00 MAD",
                R.drawable.djellaba4,
                "Djellaba  oversize avec des galons (sfifas) et un bord de capuche blancs qui contrastent fortement, et des manches en organza brodé.",
                listOf(
                    ProductVariant(Color.parseColor("#643636"), R.drawable.djellaba4),
                    ProductVariant(Color.parseColor("#6F666B"), R.drawable.djellabagris)
                ),
                sizesVetements
            )
        )

        // ---------------- BABOUCHES ----------------
        babouches = arrayListOf(
            Product(
                "Babouches “Léopard Chic”",
                "199.00 MAD",
                R.drawable.babouche4,
                "Mules confortables au motif léopard, idéales pour un look tendance et moderne.",
                listOf(
                    ProductVariant(colorBlack, R.drawable.babouche4)
                ),
                sizesChaussures
            ),
            Product(
                "Babouche en Peau de Serpent",
                "450.00 MAD",
                R.drawable.baboucherougee,
                "Mules contemporaines et élégantes avec un bout pointu, mélangeant suède rouge ou noir uni et motif serpent.",
                listOf(
                    ProductVariant(colorRed, R.drawable.baboucherougee),
                    ProductVariant(colorBlack, R.drawable.babouchenoiree)
                ),
                sizesChaussures
            ),
            Product(
                "Babouche en Velours ",
                "200.00 MAD",
                R.drawable.babouchevert,
                "Mules sophistiquées en velours  à bout pointu, embellies d'un bijou décoratif pour une touche d'élégance.",
                listOf(
                    ProductVariant(colorGreen, R.drawable.babouchevert),
                    ProductVariant(colorRed, R.drawable.baboucherouge),
                    ProductVariant(colorBlack, R.drawable.babouchenoire)
                ),
                listOf("37", "38", "39", "40", "41")
            ),
            Product(
                "Babouches Amazigh",
                "400.00 MAD",
                R.drawable.babouche3,
                "Qualité et durabilité.",
                listOf(
                    ProductVariant(Color.parseColor("#0B0347"), R.drawable.babouche3)
                ),
                sizesChaussures
            ),
            Product(
                "Babouches Idokan",
                "90.00 MAD",
                R.drawable.idokanred,
                "Chaussures-babouches plates et colorées en cuir rouge ou jaune, caractérisées par de la broderie tribale et des pompons décoratifs sur le devant.",
                listOf(
                    ProductVariant(colorRed, R.drawable.idokanred),
                    ProductVariant(colorGold, R.drawable.idokanwhite)
                ),
                sizesChaussures
            )
        )

        // ---------------- ACCESSOIRES ----------------
        accessoires = arrayListOf(
            Product(
                "Boucles d’Oreilles Luxe",
                "200.00 MAD",
                R.drawable.accessoire3,
                "De longues boucles filigranées dorées, décorées d’une pierre bleue ou vert intense pour une touche glamour.",
                listOf(
                    ProductVariant(colorBlue, R.drawable.accessoire3),
                    ProductVariant(colorGreen, R.drawable.accessoirevert)
                ),
                sizeUnique
            ),
            Product(
                "Bracelet “Amazigh Royal”",
                "300.00 MAD",
                R.drawable.accessoire5,
                "Bracelet traditionnel en argent décoré d’émail coloré et de pierres pour un style artisanal authentique.",
                listOf(
                    ProductVariant(colorBlue, R.drawable.accessoire1)
                ),
                sizeUnique
            ),
            Product(
                "Ensemble Andalus Émeraude",
                "850.00 MAD",
                R.drawable.accessoire1,
                "Un ensemble doré richement travaillé, orné de pierres vertes en forme de goutte pour un style royal et traditionnel.",
                listOf(
                    ProductVariant(colorGreen, R.drawable.accessoire1)
                ),
                sizeUnique
            ),
            Product(
                "Ensemble Perla Royale",
                "760.00 MAD",
                R.drawable.accessoire2,
                "Un set élégant composé de rangs de perles et de détails dorés incrustés de cristaux, parfait pour un look raffiné.",
                listOf(
                    ProductVariant(colorGold, R.drawable.accessoire2)
                ),
                sizeUnique
            ),
            Product(
                "Boucles d’oreilles “Arabesque Dorée",
                "100.00 MAD",
                R.drawable.accessoire4,
                "Un ensemble doré richement travaillé, orné de pierres vertes en forme de goutte pour un style royal et traditionnel.",
                listOf(
                    ProductVariant(colorGold, R.drawable.accessoire4)
                ),
                sizeUnique
            )
        )
    }
}