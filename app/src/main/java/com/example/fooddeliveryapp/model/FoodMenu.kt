package com.example.fooddeliveryapp.model

data class FoodMenu(
    val name : String? = null,
    val cost : String? = null,
    var documentId : String? = null,
    val buy : Boolean? = false
)
