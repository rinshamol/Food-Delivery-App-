package com.example.fooddeliveryapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.model.FoodMenu
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AdminRecyclerView(private val foodItems : ArrayList<FoodMenu>): RecyclerView.Adapter<AdminRecyclerView.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val item : EditText = view.findViewById(R.id.foodItem)
        val cost : EditText = view.findViewById(R.id.foodCost)
        val edit : Button = view.findViewById(R.id.foodEdit)
        val delete : Button = view.findViewById(R.id.foodDelete)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_items,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val db = Firebase.firestore
        val res = foodItems[position]
        holder.item.setText(res.name)
        holder.cost.setText(res.cost)
        holder.edit.setOnClickListener {
            val items = hashMapOf(
                "name" to holder.item.text.toString(),
                "cost" to holder.cost.text.toString()
            )
           db.collection("Food").document(res.documentId.toString()).set(items)

               .addOnSuccessListener {
                   Toast.makeText(holder.itemView.context,"Updated Successfully",
                       Toast.LENGTH_SHORT).show()
               }
               .addOnFailureListener {
                   Toast.makeText(holder.itemView.context, "Updation Failed",
                       Toast.LENGTH_SHORT).show()
               }
        }
        holder.delete.setOnClickListener {
            db.collection("Food").document(res.documentId.toString()).delete()
                .addOnSuccessListener {
                    foodItems.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position,foodItems.size-1)
                }
        }
    }

    override fun getItemCount(): Int = foodItems.size


}