package com.neosoft.neostore.models

data class EditCartmodel(
    val `data`: Boolean,
    val message: String,
    val status: Int,
    val total_carts: Int,
    val user_msg: String
)