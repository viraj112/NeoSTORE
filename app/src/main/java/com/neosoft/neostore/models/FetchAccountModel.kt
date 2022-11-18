package com.neosoft.neostore.models

data class FetchAccountModel(
    val status: Int,
    val `data`: AccountData,
    val message: String,
    val user_msg: String
)