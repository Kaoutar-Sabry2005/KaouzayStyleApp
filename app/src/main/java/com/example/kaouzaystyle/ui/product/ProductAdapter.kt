package com.example.kaouzaystyle.ui.product

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = productList[position]
                    val intent = Intent(context, ProductDetailActivity::class.java).apply {
                        putExtra("product_name", product.name)
                        // On convertit le prix (Double) en String pour l'envoyer
                        putExtra("product_price", product.price.toString())
                        putExtra("product_image_url", product.imageUrl)
                        putExtra("product_description", product.description)
                        // On passe les variantes et tailles
                        putExtra("product_variants", ArrayList(product.variants))
                        putStringArrayListExtra("product_sizes", ArrayList(product.sizes))
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

        holder.name.text = product.name

        // CORRECTION PRIX : On ajoute " MAD" à la fin du nombr
        holder.price.text = "${product.price} MAD"

        Glide.with(context)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_launcher_background) // Image d'attente
            .into(holder.img)
    }
}