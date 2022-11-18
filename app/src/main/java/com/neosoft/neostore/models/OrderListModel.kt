package com.neosoft.neostore.models

data class OrderListModel(
    val `data`: ArrayList<OrderModel>,
    val status: Int,
    val message:String,
    val user_msg :String
)