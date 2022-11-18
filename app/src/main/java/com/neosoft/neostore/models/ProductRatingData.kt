package com.neosoft.neostore.models

data class ProductRatingData(
    val cost: Int,
    val created: String,
    val description: String,
    val id: Int,
    val modified: String,
    val name: String,
    val producer: String,
    val product_category_id: Int,
    val product_images: String,
    val rating: Int,
    val view_count: Int
)