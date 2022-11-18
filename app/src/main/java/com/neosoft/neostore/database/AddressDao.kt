package com.neosoft.neostore.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AddressDao {

    @Query("select * from AddressEntity ")
    fun getAddress():List<AddressEntity>

    @Insert
    fun insertAddress(addressEntity: AddressEntity)

    @Delete
   fun deleteAddress(addressEntity: AddressEntity)
}