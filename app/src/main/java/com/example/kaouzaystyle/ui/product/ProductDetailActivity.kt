package com.example.kaouzaystyle.ui.product

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.kaouzaystyle.R
import com.example.kaouzaystyle.ProductVariant
import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.data.local.entity.ProductCart
import com.example.kaouzaystyle.data.local.entity.ProductFavorite
import com.example.kaouzaystyle.data.repository.CartRepository
import com.example.kaouzaystyle.data.repository.FavoriteRepository
import com.example.kaouzaystyle.ui.panier.CartViewModel
import com.example.kaouzaystyle.ui.panier.CartViewModelFactory
import com.example.kaouzaystyle.ui.panier.PanierActivity
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    // ViewModels et Repositories
    private lateinit var cartViewModel: CartViewModel
    private lateinit var favoriteRepository: FavoriteRepository

    // Variables d'état
    private var selectedSizeButton: Button? = null
    private var selectedColorView: View? = null
    private var currentImageUrl: String = ""
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // 1. INITIALISATION BDD & REPOS
        val db = AppDatabase.getInstance(this)

        // Setup Cart (Panier)
        val cartRepo = CartRepository(db)
        val cartFactory = CartViewModelFactory(cartRepo)
        cartViewModel = ViewModelProvider(this, cartFactory)[CartViewModel::class.java]

        // Setup Favorites (Favoris)
        favoriteRepository = FavoriteRepository(db)

        // 2. RÉCUPÉRATION DES VUES
        val imgProduct = findViewById<ImageView>(R.id.productDetailImage)
        val txtName = findViewById<TextView>(R.id.productDetailName)
        val txtPrice = findViewById<TextView>(R.id.productDetailPrice)
        val txtDesc = findViewById<TextView>(R.id.productDetailDescription)
        val layoutColors = findViewById<LinearLayout>(R.id.colorSelectionLayout)
        val layoutSizes = findViewById<LinearLayout>(R.id.sizeSelectionLayout)
        val btnAdd = findViewById<Button>(R.id.addToCartButton)
        val btnBuy = findViewById<Button>(R.id.buyNowButton)
        val btnBack = findViewById<ImageView>(R.id.backButton)

        // --- CORRECTION MAJEURE ICI ---
        // On utilise R.id.btnFavorite (l'identifiant dans le XML)
        // et non R.drawable (l'image).
        val btnFavorite = findViewById<ImageView>(R.id.btnFavorite)

        // 3. LECTURE DES DONNÉES (INTENT)
        val pName = intent.getStringExtra("product_name") ?: ""
        val pPriceString = intent.getStringExtra("product_price") ?: "0.0"
        val pDesc = intent.getStringExtra("product_description")
        currentImageUrl = intent.getStringExtra("product_image_url") ?: ""

        // Nettoyage du prix
        val priceDouble = pPriceString.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0

        @Suppress("UNCHECKED_CAST")
        val variants = intent.getSerializableExtra("product_variants") as? ArrayList<ProductVariant> ?: arrayListOf()
        val sizes = intent.getStringArrayListExtra("product_sizes") ?: arrayListOf()

        // 4. AFFICHAGE DE BASE
        txtName.text = pName
        txtPrice.text = "$pPriceString MAD"
        txtDesc.text = pDesc

        // Affichage image avec Glide
        loadProductImage(currentImageUrl, imgProduct)

        // 5. GESTION DES FAVORIS
        if (btnFavorite != null) {
            // A. Vérifier l'état au démarrage
            lifecycleScope.launch {
                isFavorite = favoriteRepository.isFavorite(pName)
                updateFavoriteIcon(btnFavorite)
            }

            // B. Clic sur le bouton favori
            btnFavorite.setOnClickListener {
                lifecycleScope.launch {
                    if (isFavorite) {
                        // Supprimer des favoris
                        favoriteRepository.removeFavorite(pName)
                        isFavorite = false
                        Toast.makeText(this@ProductDetailActivity, "Retiré des favoris", Toast.LENGTH_SHORT).show()
                    } else {
                        // Ajouter aux favoris
                        val fav = ProductFavorite(
                            name = pName,
                            price = priceDouble,
                            imageUrl = currentImageUrl
                        )
                        favoriteRepository.addFavorite(fav)
                        isFavorite = true
                        Toast.makeText(this@ProductDetailActivity, "Ajouté aux favoris ❤️", Toast.LENGTH_SHORT).show()
                    }
                    // Mettre à jour l'icône (Couleur et Forme)
                    updateFavoriteIcon(btnFavorite)
                }
            }
        }

        // 6. GÉNÉRATION DES TAILLES
        layoutSizes.removeAllViews()
        for (s in sizes) {
            val btn = Button(this)
            val params = LinearLayout.LayoutParams(dp(50), dp(50))
            params.marginEnd = dp(8)
            btn.layoutParams = params
            btn.text = s
            btn.background = ContextCompat.getDrawable(this, R.drawable.size_button_background)
            btn.setTextColor(ContextCompat.getColorStateList(this, R.color.size_button_text_color))

            btn.setOnClickListener {
                selectedSizeButton?.isSelected = false
                btn.isSelected = true
                selectedSizeButton = btn
            }
            layoutSizes.addView(btn)
        }
        if (layoutSizes.childCount > 0) {
            (layoutSizes.getChildAt(0) as Button).performClick()
        }

        // 7. GÉNÉRATION DES COULEURS
        layoutColors.removeAllViews()
        for (v in variants) {
            val view = View(this)
            val params = LinearLayout.LayoutParams(dp(40), dp(40))
            params.marginEnd = dp(12)
            view.layoutParams = params
            view.tag = v
            view.background = round(v.color, false)

            view.setOnClickListener {
                selectedColorView?.let { old ->
                    val oldV = old.tag as ProductVariant
                    old.background = round(oldV.color, false)
                }
                selectedColorView = view
                view.background = round(v.color, true)

                // Mise à jour de l'image quand on clique sur une couleur
                currentImageUrl = v.imageUrl
                loadProductImage(currentImageUrl, imgProduct)
            }
            layoutColors.addView(view)
        }
        if (layoutColors.childCount > 0) {
            layoutColors.getChildAt(0).callOnClick()
        }

        // 8. BOUTONS D'ACTION
        btnBack.setOnClickListener { finish() }

        btnAdd.setOnClickListener {
            if (handleAddToCart(pName, priceDouble)) {
                Toast.makeText(this, "Ajouté au panier", Toast.LENGTH_SHORT).show()
            }
        }

        btnBuy.setOnClickListener {
            if (handleAddToCart(pName, priceDouble)) {
                startActivity(Intent(this, PanierActivity::class.java))
            }
        }
    }

    // --- HELPER METHODS ---

    private fun handleAddToCart(name: String, price: Double): Boolean {
        if (selectedSizeButton == null) {
            Toast.makeText(this, "Veuillez choisir une taille", Toast.LENGTH_SHORT).show()
            return false
        }

        val size = selectedSizeButton?.text.toString()
        var color = Color.BLACK
        val imgUrl = currentImageUrl

        if (selectedColorView != null) {
            val v = selectedColorView?.tag as ProductVariant
            color = v.color
        }

        val item = ProductCart(
            name = name,
            price = price,
            quantity = 1,
            imageUrl = imgUrl,
            size = size,
            colorInt = color
        )
        cartViewModel.addToCart(item)
        return true
    }

    private fun loadProductImage(url: String, imageView: ImageView) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(imageView)
    }

    // --- CORRECTION ICI : Gestion complète de l'icône ---
    private fun updateFavoriteIcon(view: ImageView) {
        if (isFavorite) {
            // Favori : Cœur plein + Jaune
            // Assure-toi d'avoir le fichier res/drawable/ic_heart_filled.xml
            view.setImageResource(R.drawable.ic_heart_filled)
            view.setColorFilter(Color.parseColor("#FAB005"))
        } else {
            // Pas Favori : Cœur vide (bordure) + Gris
            // Assure-toi d'avoir le fichier res/drawable/btnfavorite.xml
            view.setImageResource(R.drawable.btnfavorite)
            view.setColorFilter(Color.GRAY)
        }
    }

    private fun round(color: Int, selected: Boolean): GradientDrawable {
        val g = GradientDrawable()
        g.cornerRadius = dp(8).toFloat()
        g.setColor(color)
        if (selected) g.setStroke(dp(2), Color.parseColor("#FAB005"))
        return g
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
}
