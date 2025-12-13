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

// Adapter pour afficher les produits dans un RecyclerView
class ProductAdapter(
    private val context: Context, // Contexte pour démarrer les activités et charger images
    private val productList: ArrayList<Product> // Liste des produits à afficher
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // ViewHolder pour chaque produit
    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.productImage) // Image du produit
        val name: TextView = view.findViewById(R.id.productName) // Nom du produit
        val price: TextView = view.findViewById(R.id.productPrice) // Prix du produit

        init {
            // Clic sur l'élément pour ouvrir la page de détail du produit
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = productList[position]
                    val intent = Intent(context, ProductDetailActivity::class.java).apply {
                        putExtra("product_name", product.name)
                        putExtra("product_price", product.price.toString()) // Prix converti en String
                        putExtra("product_image_url", product.imageUrl)
                        putExtra("product_description", product.description)
                        putExtra("product_variants", ArrayList(product.variants)) // Variantes
                        putStringArrayListExtra("product_sizes", ArrayList(product.sizes)) // Tailles
                    }
                    context.startActivity(intent) // Lancer l'activité détail produit
                }
            }
        }
    }

    // Crée un nouveau ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    // Retourne le nombre de produits
    override fun getItemCount(): Int = productList.size

    // Remplit les données dans chaque élément
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.name.text = product.name // Afficher le nom
        holder.price.text = "${product.price} MAD" // Afficher le prix avec "MAD"

        // Charger l'image du produit avec Glide
        Glide.with(context)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_launcher_background) // Image par défaut
            .into(holder.img)
    }
}
