package com.example.fooddeliveryapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.adapter.AdminRecyclerView
import com.example.fooddeliveryapp.model.FoodMenu
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MenuUploadActivity : AppCompatActivity() {
    private lateinit var item : EditText
    private lateinit var price : EditText
    private lateinit var upload : Button
    private lateinit var recycler : RecyclerView
    val foodItems = ArrayList<FoodMenu>()
    val adminRecycler = AdminRecyclerView(foodItems)
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_upload)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fetchData()
        item = findViewById(R.id.etName)
        price = findViewById(R.id.etCost)
        upload = findViewById(R.id.uploadBtn)
        recycler = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adminRecycler
        upload.setOnClickListener {
            val items = hashMapOf(
                "name" to item.text.toString(),
                "cost" to price.text.toString(),
                "buy" to false
            )
            db.collection("Food").add(items)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext,"Menu added successfully", Toast.LENGTH_SHORT).show()
                    fetchData()
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext,"Menu upload Failed", Toast.LENGTH_SHORT).show()
                }
        }


    }
    fun fetchData(){
        db.collection("Food").get()
            .addOnSuccessListener { documents->
                if (!documents.isEmpty){
                    foodItems.clear()
                    for (document in documents){
                        val data = document.toObject(FoodMenu::class.java)
                        data.documentId = document.id
                        foodItems.add(data)
                    }
                    adminRecycler.notifyDataSetChanged()
                }

            }
    }
}