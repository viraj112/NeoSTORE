package com.neosoft.neostore.models

data class ProductRatingModel(
    val `data`: ProductRatingData,
    val message: String,
    val status: Int,
    val user_msg: String
)