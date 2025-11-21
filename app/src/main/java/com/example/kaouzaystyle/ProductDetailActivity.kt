package com.example.kaouzaystyle

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

class ProductDetailActivity : AppCompatActivity() {

    private var selectedSizeButton: Button? = null
    private var selectedColorView: View? = null

    // Pour garder trace de l'image sélectionnée
    private var currentImageResId: Int = 0

    private lateinit var productDetailImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Init vues
        productDetailImage = findViewById(R.id.productDetailImage)
        val productDetailName = findViewById<TextView>(R.id.productDetailName)
        val productDetailPrice = findViewById<TextView>(R.id.productDetailPrice)
        val productDetailDescription = findViewById<TextView>(R.id.productDetailDescription)
        val addToCartButton = findViewById<Button>(R.id.addToCartButton)
        val buyNowButton = findViewById<Button>(R.id.buyNowButton)
        val colorSelectionLayout = findViewById<LinearLayout>(R.id.colorSelectionLayout)
        val sizeSelectionLayout = findViewById<LinearLayout>(R.id.sizeSelectionLayout)
        val backButton = findViewById<ImageView>(R.id.backButton)

        // Récupération données intent
        val productName = intent.getStringExtra("product_name")
        val productPrice = intent.getStringExtra("product_price")
        val description = intent.getStringExtra("product_description")
        val defaultImage = intent.getIntExtra("product_image_res_id", 0) // Default safe 0

        @Suppress("UNCHECKED_CAST")
        val variantsList = intent.getSerializableExtra("product_variants") as? ArrayList<ProductVariant>
            ?: arrayListOf()

        val sizesList = intent.getStringArrayListExtra("product_sizes") ?: arrayListOf()

        // Mise à jour UI
        productDetailName.text = productName
        productDetailPrice.text = productPrice
        productDetailDescription.text = description

        if (defaultImage != 0) {
            productDetailImage.setImageResource(defaultImage)
            currentImageResId = defaultImage
        }

        backButton.setOnClickListener { finish() }

        // ========================
        // TAILLES
        // ========================
        sizeSelectionLayout.removeAllViews()
        for (sizeStr in sizesList) {
            val btn = Button(this)
            val size48 = dpToPx(48)
            val params = LinearLayout.LayoutParams(size48, size48)
            params.marginEnd = dpToPx(8)
            btn.layoutParams = params
            btn.text = sizeStr
            btn.background = ContextCompat.getDrawable(this, R.drawable.size_button_background)
            btn.setTextColor(ContextCompat.getColorStateList(this, R.color.size_button_text_color))
            // Remove padding pour centrer si besoin
            btn.setPadding(0,0,0,0)

            btn.setOnClickListener {
                selectedSizeButton?.isSelected = false
                btn.isSelected = true
                selectedSizeButton = btn
            }
            sizeSelectionLayout.addView(btn)
        }

        // ========================
        // COULEURS (40x40, Radius 20%)
        // ========================
        colorSelectionLayout.removeAllViews()
        val boxSize = dpToPx(40)
        val margin = dpToPx(16)

        for (variant in variantsList) {
            val colorBox = View(this)
            val params = LinearLayout.LayoutParams(boxSize, boxSize)
            params.marginEnd = margin
            colorBox.layoutParams = params

            colorBox.tag = variant

            // État initial : Rectangle arrondi sans bordure
            colorBox.background = createRoundedShape(variant.colorInt, false)

            colorBox.setOnClickListener {
                // Réinitialiser l'ancien
                selectedColorView?.let { oldView ->
                    val oldVariant = oldView.tag as ProductVariant
                    oldView.background = createRoundedShape(oldVariant.colorInt, false)
                }

                // Activer nouveau
                selectedColorView = colorBox
                colorBox.background = createRoundedShape(variant.colorInt, true) // Bordure Or

                // CHANGER IMAGE
                productDetailImage.setImageResource(variant.imageResId)
                currentImageResId = variant.imageResId
            }
            colorSelectionLayout.addView(colorBox)
        }

        // ========================
        // BOUTONS ACTIONS
        // ========================
        addToCartButton.setOnClickListener {
            if (checkSelection()) {
                // Simulation ajout panier sans redirection
                Toast.makeText(this, "Produit ajouté au panier avec succès", Toast.LENGTH_SHORT).show()
            }
        }

        buyNowButton.setOnClickListener {
            if (checkSelection()) {
                // Redirection vers la page panier vide
                val intentPanier = Intent(this, CartActivity::class.java)
                startActivity(intentPanier)
            }
        }
    }

    private fun checkSelection(): Boolean {
        if (selectedSizeButton == null) {
            Toast.makeText(this, "Veuillez choisir une taille", Toast.LENGTH_SHORT).show()
            return false
        }
        if (selectedColorView == null) {
            Toast.makeText(this, "Veuillez choisir une couleur", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // Forme : Dimension 40dp est définie dans LayoutParams
    // Ici on gère la forme : RECTANGLE + Radius 20% (de 40dp) = 8dp
    private fun createRoundedShape(color: Int, isSelected: Boolean): GradientDrawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE

        // Radius de 20% de la taille (supposée 40dp -> ~8dp)
        // On calcule 8dp en pixels
        val radiusPx = dpToPx(8).toFloat()
        shape.cornerRadius = radiusPx

        shape.setColor(color)

        if (isSelected) {
            // Bordure Gold
            val goldColor = Color.parseColor("#FAB005")
            shape.setStroke(dpToPx(2), goldColor)
        } else {
            shape.setStroke(0, Color.TRANSPARENT)
        }
        return shape
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}