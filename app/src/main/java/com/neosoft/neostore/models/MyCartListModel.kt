package com.neosoft.neostore.models

data class MyCartListModel(
    val `data`: ArrayList<Data>,
    val count: Int,
    val status: Int,
    val total: Double,
    val user_message :String
)