package com.example.fooddeliveryapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.model.FoodMenu
import com.google.android.gms.common.SignInButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CustomerRecycler(private val menuItem : ArrayList<FoodMenu>) : RecyclerView.Adapter<CustomerRecycler.ViewHolder>() {
    class  ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val item : TextView = view.findViewById(R.id.menuItem)
        val cost : TextView = view.findViewById(R.id.menuCost)
        val buy : Button = view.findViewById(R.id.buyBtn)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_items,parent,false)
       return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val db = Firebase.firestore
        val res = menuItem[position]
        holder.item.text = res.name
        holder.cost.text = res.cost
        holder.buy.setOnClickListener {
            val items = hashMapOf(
                "buy" to true
            )
            db.collection("Food").document(res.documentId.toString()).set(items)
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context,"${holder.item.text} bought Successfully",
                        Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(holder.itemView.context,"${holder.item.text} can't buy now",
                        Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun getItemCount(): Int = menuItem.size

}