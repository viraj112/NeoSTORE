package com.neosoft.neostore.models

data class MyCartListModel(
    val count: Int,
    val `data`: List<Data>,
    val status: Int,
    val total: Double,
    val user_message :String
)