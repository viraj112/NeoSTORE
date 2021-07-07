package com.neosoft.neostore.models

data class  OrderDetailsModel(
    val `data`: OrderDetailsModel?,
    val order_details:ArrayList<OrderDetails>,
    val id:Int,
    val cost:Int,
    val created:String,
    val message:String,
    val user_message:String
)