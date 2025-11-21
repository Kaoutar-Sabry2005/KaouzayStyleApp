package com.example.kaouzaystyle

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Bouton retour si nécessaire
        val backBtn = findViewById<ImageView>(R.id.cartBackButton)
        backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}