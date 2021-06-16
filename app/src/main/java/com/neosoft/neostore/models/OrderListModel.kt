package com.neosoft.neostore.models

data class OrderListModel(
    val `data`: List<OrderModel>,
    val status: Int,
    val message:String,
    val user_msg :String
)