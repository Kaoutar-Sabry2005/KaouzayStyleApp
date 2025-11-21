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

    // UI variables
    private lateinit var menuCaftan: TextView
    private lateinit var menuDjellaba: TextView
    private lateinit var menuBabouches: TextView
    private lateinit var menuAccessoires: TextView

    private lateinit var underlineCaftan: View
    private lateinit var underlineDjellaba: View
    private lateinit var underlineBabouches: View
    private lateinit var underlineAccessoires: View

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

        // Initialisation des vues (Catégories haut)
        menuCaftan = findViewById(R.id.menuCaftan)
        menuDjellaba = findViewById(R.id.menuDjellaba)
        menuBabouches = findViewById(R.id.menuBabouches)
        menuAccessoires = findViewById(R.id.menuAccessoires)

        underlineCaftan = findViewById(R.id.underlineCaftan)
        underlineDjellaba = findViewById(R.id.underlineDjellaba)
        underlineBabouches = findViewById(R.id.underlineBabouches)
        underlineAccessoires = findViewById(R.id.underlineAccessoires)

        // Initialisation des vues (Menu bas)
        menuAccueil = findViewById(R.id.menuAccueil)
        menuCategorie = findViewById(R.id.menuCategorie)
        menuPanier = findViewById(R.id.menuPanier)
        menuProfil = findViewById(R.id.menuProfil)

        iconAccueil = findViewById(R.id.iconAccueil)
        iconCategorie = findViewById(R.id.iconCategorie)
        iconProfil = findViewById(R.id.iconProfil)

        // État initial
        showProducts(caftans)
        activateCategory(menuCaftan, underlineCaftan)
        activateBottom(menuAccueil, iconAccueil)

        // Listeners Catégories
        menuCaftan.setOnClickListener {
            showProducts(caftans)
            activateCategory(menuCaftan, underlineCaftan)
        }
        menuDjellaba.setOnClickListener {
            showProducts(djellabas)
            activateCategory(menuDjellaba, underlineDjellaba)
        }
        menuBabouches.setOnClickListener {
            showProducts(babouches)
            activateCategory(menuBabouches, underlineBabouches)
        }
        menuAccessoires.setOnClickListener {
            showProducts(accessoires)
            activateCategory(menuAccessoires, underlineAccessoires)
        }

        // Listeners Bas
        menuAccueil.setOnClickListener {
            showProducts(caftans)
            activateCategory(menuCaftan, underlineCaftan)
            activateBottom(menuAccueil, iconAccueil)
        }
        menuCategorie.setOnClickListener { activateBottom(menuCategorie, iconCategorie) }
    }

    private fun showProducts(list: ArrayList<Product>) {
        recyclerProducts.adapter = ProductAdapter(this, list)
    }

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
                    // Correction ICI : Color.parseColor(...)
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
                    // Corrections ICI pour toutes les couleurs Hex
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
                    // Corrections ICI
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
                    // Correction ICI
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
                    // Correction ICI
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
                    // Corrections ICI
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
                    // Correction ICI
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

        menuAccueil.setTextColor(gray)
        menuCategorie.setTextColor(gray)
        menuPanier.setTextColor(gray)
        menuProfil.setTextColor(gray)
        menuAccueil.setBackgroundColor(dark)
        menuCategorie.setBackgroundColor(dark)
        menuPanier.setBackgroundColor(dark)
        menuProfil.setBackgroundColor(dark)
        iconAccueil.setColorFilter(gray)
        iconCategorie.setColorFilter(gray)
        iconProfil.setColorFilter(gray)

        selected.setTextColor(Color.BLACK)
        icon.setColorFilter(Color.BLACK)
        selected.setBackgroundColor(Color.WHITE)
    }
}