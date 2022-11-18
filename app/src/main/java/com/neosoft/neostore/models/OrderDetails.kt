package com.neosoft.neostore.models

data class OrderDetails(
    val id: Int,
    val order_id: Int,
    val product_id: Int,
    val quantity: Int,
    val total: Int,
    val prod_name: String,
    val prod_cat_name: String,
    val prod_image: String
)