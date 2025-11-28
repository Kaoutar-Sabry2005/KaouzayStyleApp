package com.example.kaouzaystyle

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var subtotalAmount: TextView
    private lateinit var shippingAmount: TextView
    private lateinit var totalAmount: TextView
    private lateinit var checkoutButton: Button
    private val shippingFee = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        subtotalAmount = findViewById(R.id.subtotalAmount)
        shippingAmount = findViewById(R.id.shippingAmount)
        totalAmount = findViewById(R.id.totalAmount)
        checkoutButton = findViewById(R.id.checkoutButton)

        val adapter = CartAdapter(CartManager.cartItems) { updateSummary() }
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartRecyclerView.adapter = adapter

        updateSummary()

        checkoutButton.setOnClickListener {
            if (CartManager.cartItems.isEmpty()) {
                Toast.makeText(this, "Votre panier est vide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
    }


    private fun updateSummary() {
        val subtotal = CartManager.cartItems.sumOf { it.price * it.quantity }
        subtotalAmount.text = "%.2f MAD".format(subtotal)
        shippingAmount.text = "%.2f MAD".format(shippingFee)
        totalAmount.text = "%.2f MAD".format(subtotal + shippingFee)
    }
}
