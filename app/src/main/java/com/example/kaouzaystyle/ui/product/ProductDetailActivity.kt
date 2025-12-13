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
import com.example.kaouzaystyle.util.NotificationHelper
import kotlinx.coroutines.launch

// Activité pour afficher les détails d'un produit et gérer le panier/favoris
class ProductDetailActivity : AppCompatActivity() {

    private lateinit var cartViewModel: CartViewModel // ViewModel du panier
    private lateinit var favoriteRepository: FavoriteRepository // Repository des favoris

    private var selectedSizeButton: Button? = null // Taille sélectionnée
    private var selectedColorView: View? = null // Couleur sélectionnée
    private var currentImageUrl: String = "" // URL de l'image affichée
    private var isFavorite = false // Indique si le produit est favori

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val db = AppDatabase.getInstance(this)

        // Initialiser ViewModel du panier
        val cartRepo = CartRepository(db)
        val cartFactory = CartViewModelFactory(application, cartRepo)
        cartViewModel = ViewModelProvider(this, cartFactory)[CartViewModel::class.java]

        // Initialiser le repository des favoris
        favoriteRepository = FavoriteRepository(db)

        // Récupérer les vues
        val imgProduct = findViewById<ImageView>(R.id.productDetailImage)
        val txtName = findViewById<TextView>(R.id.productDetailName)
        val txtPrice = findViewById<TextView>(R.id.productDetailPrice)
        val txtDesc = findViewById<TextView>(R.id.productDetailDescription)
        val layoutColors = findViewById<LinearLayout>(R.id.colorSelectionLayout)
        val layoutSizes = findViewById<LinearLayout>(R.id.sizeSelectionLayout)
        val btnAdd = findViewById<Button>(R.id.addToCartButton)
        val btnBuy = findViewById<Button>(R.id.buyNowButton)
        val btnBack = findViewById<ImageView>(R.id.backButton)
        val btnFavorite = findViewById<ImageView>(R.id.btnFavorite)

        // Récupérer les données passées via Intent
        val pName = intent.getStringExtra("product_name") ?: ""
        val pPriceString = intent.getStringExtra("product_price") ?: "0.0"
        val pDesc = intent.getStringExtra("product_description")
        currentImageUrl = intent.getStringExtra("product_image_url") ?: ""

        val priceDouble = pPriceString.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0

        @Suppress("UNCHECKED_CAST")
        val variants = intent.getSerializableExtra("product_variants") as? ArrayList<ProductVariant> ?: arrayListOf()
        val sizes = intent.getStringArrayListExtra("product_sizes") ?: arrayListOf()

        // Remplir les vues avec les données
        txtName.text = pName
        txtPrice.text = "$pPriceString MAD"
        txtDesc.text = pDesc
        loadProductImage(currentImageUrl, imgProduct)

        // Gestion des favoris
        if (btnFavorite != null) {
            lifecycleScope.launch {
                isFavorite = favoriteRepository.isFavorite(pName) // Vérifier si favori
                updateFavoriteIcon(btnFavorite) // Mettre à jour l'icône
            }

            btnFavorite.setOnClickListener {
                lifecycleScope.launch {
                    if (isFavorite) {
                        favoriteRepository.removeFavorite(pName) // Supprimer des favoris
                        isFavorite = false
                        Toast.makeText(this@ProductDetailActivity, "Retiré des favoris", Toast.LENGTH_SHORT).show()
                    } else {
                        val fav = ProductFavorite(name = pName, price = priceDouble, imageUrl = currentImageUrl)
                        favoriteRepository.addFavorite(fav) // Ajouter aux favoris
                        isFavorite = true
                        Toast.makeText(this@ProductDetailActivity, "Ajouté aux favoris ❤️", Toast.LENGTH_SHORT).show()
                    }
                    updateFavoriteIcon(btnFavorite) // Mettre à jour l'icône
                }
            }
        }

        // Générer les boutons de taille dynamiquement
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
            (layoutSizes.getChildAt(0) as Button).performClick() // Sélectionner la première taille par défaut
        }

        // Générer les vues de couleur dynamiquement
        layoutColors.removeAllViews()
        for (v in variants) {
            val view = View(this)
            val params = LinearLayout.LayoutParams(dp(40), dp(40))
            params.marginEnd = dp(12)
            view.layoutParams = params
            view.tag = v
            view.background = round(v.color, false) // Affichage couleur

            view.setOnClickListener {
                // Déselectionner l'ancienne couleur
                selectedColorView?.let { old ->
                    val oldV = old.tag as ProductVariant
                    old.background = round(oldV.color, false)
                }
                selectedColorView = view
                view.background = round(v.color, true) // Sélectionner nouvelle couleur

                currentImageUrl = v.imageUrl
                loadProductImage(currentImageUrl, imgProduct) // Mettre à jour l'image
            }
            layoutColors.addView(view)
        }
        if (layoutColors.childCount > 0) {
            layoutColors.getChildAt(0).callOnClick() // Sélectionner la première couleur par défaut
        }

        // Bouton retour
        btnBack.setOnClickListener { finish() }

        // Ajouter au panier
        btnAdd.setOnClickListener {
            if (handleAddToCart(pName, priceDouble)) {
                Toast.makeText(this, "Ajouté au panier", Toast.LENGTH_SHORT).show()
            }
        }

        // Acheter maintenant
        btnBuy.setOnClickListener {
            if (handleAddToCart(pName, priceDouble)) {
                startActivity(Intent(this, PanierActivity::class.java))
            }
        }
    }

    // Ajouter un produit au panier
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

        // Envoyer une notification d'ajout au panier
        NotificationHelper.sendCartNotification(this, name)

        return true
    }

    // Charger une image dans un ImageView
    private fun loadProductImage(url: String, imageView: ImageView) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_launcher_background) // Image par défaut
            .error(R.drawable.ic_launcher_background) // Image si erreur
            .into(imageView)
    }

    // Mettre à jour l'icône favori
    private fun updateFavoriteIcon(view: ImageView) {
        if (isFavorite) {
            view.setImageResource(R.drawable.ic_heart_filled)
            view.setColorFilter(Color.parseColor("#FAB005"))
        } else {
            view.setImageResource(R.drawable.btnfavorite)
            view.setColorFilter(Color.GRAY)
        }
    }

    // Créer un fond arrondi pour la couleur
    private fun round(color: Int, selected: Boolean): GradientDrawable {
        val g = GradientDrawable()
        g.cornerRadius = dp(8).toFloat()
        g.setColor(color)
        if (selected) g.setStroke(dp(2), Color.parseColor("#FAB005")) // Bordure si sélectionné
        return g
    }

    // Convertir dp en pixels
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
}
