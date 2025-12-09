package com.example.kaouzaystyle.ui.product

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kaouzaystyle.Product
import com.example.kaouzaystyle.R

class ProductAdapter(
    private val context: Context,
    private val productList: ArrayList<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.productImage)
        val name: TextView = view.findViewById(R.id.productName)
        val price: TextView = view.findViewById(R.id.productPrice)

        // Dans ProductAdapter.kt

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = productList[position]
                    val intent = Intent(context, ProductDetailActivity::class.java).apply {
                        putExtra("product_name", product.name)
                        putExtra("product_price", product.price)
                        putExtra("product_image_res_id", product.image)
                        putExtra("product_description", product.description)

                        // CORRECTION ICI : On utilise variants et non plus availableColors
                        // On cast en ArrayList pour que putSerializableExtra fonctionne bien
                        putExtra("product_variants", ArrayList(product.variants))

                        // On passe les tailles
                        putStringArrayListExtra("product_sizes", ArrayList(product.availableSizes))
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.img.setImageResource(product.image)
        holder.name.text = product.name
        holder.price.text = product.price
    }
}