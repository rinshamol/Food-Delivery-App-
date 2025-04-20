package com.example.fooddeliveryapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.adapter.CustomerRecycler
import com.example.fooddeliveryapp.model.FoodMenu
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class DashboardActivity : AppCompatActivity() {
    private lateinit var searchItem : EditText
    private lateinit var searchBtn : Button
    private lateinit var recycler: RecyclerView
    val foodMenu = ArrayList<FoodMenu>()
    val recyclerAdapter = CustomerRecycler(foodMenu)
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        fetchData()
        searchItem = findViewById(R.id.searchEt)
        searchBtn = findViewById(R.id.searchBtn)
        recycler = findViewById(R.id.custRecycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = recyclerAdapter
        searchBtn.setOnClickListener {
            db.collection("Food").whereEqualTo("name",searchItem.text.toString()).get()
                .addOnSuccessListener { documents->
                    if(!documents.isEmpty){
                        foodMenu.clear()
                        for (document in documents) {
                            val data = document.toObject(FoodMenu::class.java)
                            data.documentId = document.id
                            foodMenu.add(data)
                        }
                        recyclerAdapter.notifyDataSetChanged()
                    }

                }
        }

    }
    fun fetchData(){
        db.collection("Food").get()
            .addOnSuccessListener { documents->
                if(!documents.isEmpty){
                    foodMenu.clear()
                    for (document in documents) {
                        val data = document.toObject(FoodMenu::class.java)
                        data.documentId = document.id
                        foodMenu.add(data)
                    }
                    recyclerAdapter.notifyDataSetChanged()
                }

            }
    }
}