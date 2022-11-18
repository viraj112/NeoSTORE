package com.neosoft.neostore.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    var id :Int=0,
     val name: String,
    val address: String
)