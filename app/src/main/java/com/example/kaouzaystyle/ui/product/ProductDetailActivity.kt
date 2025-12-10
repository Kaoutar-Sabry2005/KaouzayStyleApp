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
import com.bumptech.glide.Glide
import com.example.kaouzaystyle.R
import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.data.local.entity.ProductCart
import com.example.kaouzaystyle.data.repository.CartRepository
import com.example.kaouzaystyle.ProductVariant
import com.example.kaouzaystyle.ui.panier.PanierActivity
import com.example.kaouzaystyle.ui.panier.CartViewModel
import com.example.kaouzaystyle.ui.panier.CartViewModelFactory

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: CartViewModel
    private var selectedSizeButton: Button? = null
    private var selectedColorView: View? = null
    private var currentImageUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val db = AppDatabase.getInstance(this)
        val repository = CartRepository(db)
        val factory = CartViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CartViewModel::class.java]

        val imgProduct = findViewById<ImageView>(R.id.productDetailImage)
        val txtName = findViewById<TextView>(R.id.productDetailName)
        val txtPrice = findViewById<TextView>(R.id.productDetailPrice)
        val txtDesc = findViewById<TextView>(R.id.productDetailDescription)
        val layoutColors = findViewById<LinearLayout>(R.id.colorSelectionLayout)
        val layoutSizes = findViewById<LinearLayout>(R.id.sizeSelectionLayout)
        val btnAdd = findViewById<Button>(R.id.addToCartButton)
        val btnBuy = findViewById<Button>(R.id.buyNowButton)
        val btnBack = findViewById<ImageView>(R.id.backButton)

        val pName = intent.getStringExtra("product_name") ?: ""
        // On récupère le prix brut (ex: "7200.0") et on l'affiche avec " MAD"
        val pPriceString = intent.getStringExtra("product_price") ?: "0.0"
        val pDesc = intent.getStringExtra("product_description")
        currentImageUrl = intent.getStringExtra("product_image_url") ?: ""

        @Suppress("UNCHECKED_CAST")
        val variants = intent.getSerializableExtra("product_variants") as? ArrayList<ProductVariant> ?: arrayListOf()
        val sizes = intent.getStringArrayListExtra("product_sizes") ?: arrayListOf()

        txtName.text = pName
        txtPrice.text = "$pPriceString MAD" // Affichage correct
        txtDesc.text = pDesc

        Glide.with(this).load(currentImageUrl).into(imgProduct)

        btnBack.setOnClickListener { finish() }

        val priceDouble = pPriceString.toDoubleOrNull() ?: 0.0

        // TAILLES
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
        if (layoutSizes.childCount > 0) (layoutSizes.getChildAt(0) as Button).performClick()

        // COULEURS
        layoutColors.removeAllViews()
        for (v in variants) {
            val view = View(this)
            val params = LinearLayout.LayoutParams(dp(40), dp(40))
            params.marginEnd = dp(12)
            view.layoutParams = params
            view.tag = v

            // Utilisation de la propriété .color (qui fait la conversion Hex -> Int)
            view.background = round(v.color, false)

            view.setOnClickListener {
                selectedColorView?.let { old ->
                    val oldV = old.tag as ProductVariant
                    old.background = round(oldV.color, false)
                }
                selectedColorView = view
                view.background = round(v.color, true)

                Glide.with(this).load(v.imageUrl).into(imgProduct)
                currentImageUrl = v.imageUrl
            }
            layoutColors.addView(view)
        }
        if (layoutColors.childCount > 0) layoutColors.getChildAt(0).callOnClick()

        btnAdd.setOnClickListener {
            handleAddToCart(pName, priceDouble)
            Toast.makeText(this, "Ajouté au panier", Toast.LENGTH_SHORT).show()
        }

        btnBuy.setOnClickListener {
            handleAddToCart(pName, priceDouble)
            startActivity(Intent(this, PanierActivity::class.java))
        }
    }

    private fun handleAddToCart(name: String, price: Double) {
        val size = selectedSizeButton?.text.toString() ?: ""
        var color = Color.BLACK
        var img = currentImageUrl

        if (selectedColorView != null) {
            val v = selectedColorView?.tag as ProductVariant
            color = v.color
            img = v.imageUrl
        }

        val item = ProductCart(
            name = name,
            price = price,
            quantity = 1,
            imageUrl = img,
            size = size,
            colorInt = color
        )
        viewModel.addToCart(item)
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