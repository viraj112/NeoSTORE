package com.neosoft.neostore.models

data class Product(
    val cost: Int,
    val id: Int,
    val name: String,
    val product_category: String,
    val product_images: String,
    val sub_total: Int,
    val total:String

)