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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Récupération vues
        val imgProduct = findViewById<ImageView>(R.id.productDetailImage)
        val txtName = findViewById<TextView>(R.id.productDetailName)
        val txtPrice = findViewById<TextView>(R.id.productDetailPrice)
        val txtDesc = findViewById<TextView>(R.id.productDetailDescription)
        val layoutColors = findViewById<LinearLayout>(R.id.colorSelectionLayout)
        val layoutSizes = findViewById<LinearLayout>(R.id.sizeSelectionLayout)
        val btnAdd = findViewById<Button>(R.id.addToCartButton)
        val btnBuy = findViewById<Button>(R.id.buyNowButton)
        val btnBack = findViewById<ImageView>(R.id.backButton)

        // Récupération Intent
        val pName = intent.getStringExtra("product_name") ?: "Produit"
        val pPrice = intent.getStringExtra("product_price") ?: "0.0"
        val pDesc = intent.getStringExtra("product_description")
        val pImg = intent.getIntExtra("product_image_res_id", 0)

        @Suppress("UNCHECKED_CAST")
        val variants = intent.getSerializableExtra("product_variants") as? ArrayList<ProductVariant> ?: arrayListOf()
        val sizes = intent.getStringArrayListExtra("product_sizes") ?: arrayListOf()

        // Affichage
        txtName.text = pName
        txtPrice.text = pPrice
        txtDesc.text = pDesc
        if (pImg != 0) imgProduct.setImageResource(pImg)
        currentImageResId = pImg

        btnBack.setOnClickListener { finish() }

        // Nettoyage Prix (Enlever "MAD" ou espaces pour avoir un Double)
        val cleanPrice = pPrice.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0

        // --- GÉNÉRATION TAILLES ---
        layoutSizes.removeAllViews()
        for (s in sizes) {
            val btn = Button(this)
            val params = LinearLayout.LayoutParams(dpToPx(48), dpToPx(48))
            params.marginEnd = dpToPx(8)
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
        // Sélection par défaut (1er bouton)
        if (layoutSizes.childCount > 0) {
            (layoutSizes.getChildAt(0) as Button).performClick()
        }

        // --- GÉNÉRATION COULEURS ---
        layoutColors.removeAllViews()
        for (v in variants) {
            val view = View(this)
            val params = LinearLayout.LayoutParams(dpToPx(40), dpToPx(40))
            params.marginEnd = dpToPx(16)
            view.layoutParams = params
            view.tag = v // IMPORTANT: On stocke le variant dans le tag

            view.background = createRoundedShape(v.colorInt, false)

            view.setOnClickListener {
                // Reset ancien
                selectedColorView?.let { old ->
                    val oldV = old.tag as ProductVariant
                    old.background = createRoundedShape(oldV.colorInt, false)
                }
                // Activer nouveau
                selectedColorView = view
                view.background = createRoundedShape(v.colorInt, true)

                // Changer image
                imgProduct.setImageResource(v.imageResId)
                currentImageResId = v.imageResId
            }
            layoutColors.addView(view)
        }
        // Sélection par défaut (1ere couleur)
        if (layoutColors.childCount > 0) {
            layoutColors.getChildAt(0).callOnClick()
        }

        // --- CLIC AJOUTER PANIER ---
        btnAdd.setOnClickListener {
            if (addToCartLogic(pName, cleanPrice)) {
                Toast.makeText(this, "Produit ajouté au panier !", Toast.LENGTH_SHORT).show()
            }
        }

        // --- CLIC ACHETER ---
        btnBuy.setOnClickListener {
            if (addToCartLogic(pName, cleanPrice)) {
                val intent = Intent(this, PanierActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // Fonction commune pour éviter de dupliquer le code
    private fun addToCartLogic(name: String, price: Double): Boolean {
        if (selectedSizeButton == null || selectedColorView == null) {
            Toast.makeText(this, "Chargement en cours, réessayez...", Toast.LENGTH_SHORT).show()
            return false
        }

        val sizeVal = selectedSizeButton?.text.toString()
        val variant = selectedColorView?.tag as? ProductVariant
        val colorVal = variant?.colorInt ?: Color.BLACK // Fallback
        val finalImg = variant?.imageResId ?: currentImageResId

        val item = CartItem(
            name = name,
            price = price,
            imageResId = finalImg,
            size = sizeVal,
            colorInt = colorVal,
            quantity = 1
        )

        CartManager.addToCart(item)
        return true
    }

    private fun createRoundedShape(color: Int, selected: Boolean): GradientDrawable {
        val d = GradientDrawable()
        d.shape = GradientDrawable.RECTANGLE
        d.cornerRadius = dpToPx(8).toFloat()
        d.setColor(color)
        if (selected) d.setStroke(dpToPx(2), Color.parseColor("#FAB005"))
        return d
    }

    private fun dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()
}
