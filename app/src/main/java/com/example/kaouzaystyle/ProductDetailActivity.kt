package com.example.kaouzaystyle

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ProductDetailActivity : AppCompatActivity() {

    private var selectedSizeButton: Button? = null
    private var selectedColorView: View? = null
    private var currentImageResId: Int = 0
    private lateinit var productDetailImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Views
        productDetailImage = findViewById(R.id.productDetailImage)
        val productDetailName = findViewById<TextView>(R.id.productDetailName)
        val productDetailPrice = findViewById<TextView>(R.id.productDetailPrice)
        val productDetailDescription = findViewById<TextView>(R.id.productDetailDescription)
        val addToCartButton = findViewById<Button>(R.id.addToCartButton)
        val buyNowButton = findViewById<Button>(R.id.buyNowButton)
        val colorSelectionLayout = findViewById<LinearLayout>(R.id.colorSelectionLayout)
        val sizeSelectionLayout = findViewById<LinearLayout>(R.id.sizeSelectionLayout)
        val backButton = findViewById<ImageView>(R.id.backButton)

        // Intent data
        val productName = intent.getStringExtra("product_name")
        val productPriceStr = intent.getStringExtra("product_price") ?: "0.0"
        val description = intent.getStringExtra("product_description")
        val defaultImage = intent.getIntExtra("product_image_res_id", 0)

        // Extract numeric price
        val productPriceValue =
            productPriceStr.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0

        @Suppress("UNCHECKED_CAST")
        val variantsList = intent.getSerializableExtra("product_variants") as? ArrayList<ProductVariant>
            ?: arrayListOf()

        val sizesList = intent.getStringArrayListExtra("product_sizes") ?: arrayListOf()

        // Update UI
        productDetailName.text = productName
        productDetailPrice.text = "%.2f MAD".format(productPriceValue)
        productDetailDescription.text = description

        if (defaultImage != 0) {
            productDetailImage.setImageResource(defaultImage)
            currentImageResId = defaultImage
        }

        backButton.setOnClickListener { finish() }

        // ------------ SIZE SELECTION ---------------
        sizeSelectionLayout.removeAllViews()
        for (sizeStr in sizesList) {
            val btn = Button(this)
            val size48 = dpToPx(48)
            val params = LinearLayout.LayoutParams(size48, size48)
            params.marginEnd = dpToPx(8)
            btn.layoutParams = params
            btn.text = sizeStr
            btn.background =
                ContextCompat.getDrawable(this, R.drawable.size_button_background)
            btn.setTextColor(
                ContextCompat.getColorStateList(
                    this,
                    R.color.size_button_text_color
                )
            )
            btn.setPadding(0, 0, 0, 0)
            btn.setOnClickListener {
                selectedSizeButton?.isSelected = false
                btn.isSelected = true
                selectedSizeButton = btn
            }
            sizeSelectionLayout.addView(btn)
        }

        // ------------ COLOR SELECTION ---------------
        colorSelectionLayout.removeAllViews()
        val boxSize = dpToPx(40)
        val margin = dpToPx(16)

        for (variant in variantsList) {
            val colorBox = View(this)
            val params = LinearLayout.LayoutParams(boxSize, boxSize)
            params.marginEnd = margin
            colorBox.layoutParams = params
            colorBox.tag = variant
            colorBox.background = createRoundedShape(variant.colorInt, false)

            colorBox.setOnClickListener {
                selectedColorView?.let { oldView ->
                    val oldVariant = oldView.tag as ProductVariant
                    oldView.background = createRoundedShape(oldVariant.colorInt, false)
                }
                selectedColorView = colorBox
                colorBox.background = createRoundedShape(variant.colorInt, true)

                productDetailImage.setImageResource(variant.imageResId)
                currentImageResId = variant.imageResId
            }

            colorSelectionLayout.addView(colorBox)
        }

        // ------------ ADD TO CART ---------------
        addToCartButton.setOnClickListener {
            if (checkSelection()) {
                val selectedColorInt =
                    (selectedColorView?.tag as? ProductVariant)?.colorInt
                        ?: Color.WHITE

                CartManager.addToCart(
                    CartItem(
                        name = productName ?: "",
                        price = productPriceValue,
                        imageResId = currentImageResId,
                        size = selectedSizeButton?.text.toString(),
                        color = selectedColorInt
                    )
                )

                Toast.makeText(
                    this,
                    "Produit ajouté au panier avec succès",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // ------------ BUY NOW (Add + Go to Cart) ---------------
        buyNowButton.setOnClickListener {
            if (checkSelection()) {
                val selectedColorInt =
                    (selectedColorView?.tag as? ProductVariant)?.colorInt
                        ?: Color.WHITE

                CartManager.addToCart(
                    CartItem(
                        name = productName ?: "",
                        price = productPriceValue,
                        imageResId = currentImageResId,
                        size = selectedSizeButton?.text.toString(),
                        color = selectedColorInt
                    )
                )

                // Lancer la bonne activité
                startActivity(Intent(this, PanierActivity::class.java))
            }
        }

    }

    // Vérification taille + couleur
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

    private fun createRoundedShape(color: Int, isSelected: Boolean): GradientDrawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = dpToPx(8).toFloat()
        shape.setColor(color)

        if (isSelected)
            shape.setStroke(dpToPx(2), Color.parseColor("#FAB005"))
        else
            shape.setStroke(0, Color.TRANSPARENT)

        return shape
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
