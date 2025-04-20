package com.example.fooddeliveryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var admin : Button
    private lateinit var customers : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        admin = findViewById(R.id.admin)
        customers = findViewById(R.id.customers)

        customers.setOnClickListener {
            var custIntent = Intent(this, CustomerActivity::class.java);
            startActivity(custIntent)
        }
        admin.setOnClickListener {
            var adminIntent = Intent(this, MenuUploadActivity::class.java)
            startActivity(adminIntent)
        }
    }
}