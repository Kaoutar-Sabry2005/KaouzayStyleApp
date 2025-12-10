package com.example.kaouzaystyle.ui.favorite

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kaouzaystyle.R
import com.example.kaouzaystyle.data.local.database.AppDatabase
import com.example.kaouzaystyle.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    private lateinit var adapter: FavoritesAdapter
    private lateinit var repo: FavoriteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Init DB & Repo
        val db = AppDatabase.getInstance(this)
        repo = FavoriteRepository(db)

        // Init RecyclerView
        val recycler = findViewById<RecyclerView>(R.id.recyclerFavorites)
        val btnBack = findViewById<ImageView>(R.id.btnBackFav)

        recycler.layoutManager = LinearLayoutManager(this)

        // Init Adapter
        adapter = FavoritesAdapter(arrayListOf()) { productToDelete ->
            // Action quand on clique sur le cœur pour supprimer
            lifecycleScope.launch {
                repo.removeFavorite(productToDelete.name)
                Toast.makeText(this@FavoritesActivity, "Supprimé des favoris", Toast.LENGTH_SHORT).show()
            }
        }
        recycler.adapter = adapter

        // Observer la base de données
        lifecycleScope.launch {
            repo.getAllFavorites().collectLatest { list ->
                adapter.submitList(list)
            }
        }

        btnBack.setOnClickListener { finish() }
    }
}