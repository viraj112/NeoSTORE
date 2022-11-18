package com.neosoft.neostore.models

data class LoginModel(
    val `data`: LoginData,
    val message: String,
    val status: Int,
    val user_msg: String
    )
