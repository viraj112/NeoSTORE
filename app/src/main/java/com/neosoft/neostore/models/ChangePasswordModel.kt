package com.neosoft.neostore.models

data class ChangePasswordModel(
    val `data`: List<Any>,
    val message: String,
    val status: Int,
    val user_msg: String
)