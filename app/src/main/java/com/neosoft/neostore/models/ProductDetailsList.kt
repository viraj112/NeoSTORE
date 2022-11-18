package com.neosoft.neostore.models

data class ProductDetailsList(
    val id: Int,
    val product_category_id: Int,
    val name: String,
    val producer: String,
    val description: String,
    val cost: Int,
    val rating: Int,
    val view_count: Int,
    val created: String,
    val modified: String
)