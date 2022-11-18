package com.neosoft.neostore.api

import androidx.room.Dao
import androidx.room.Insert
import com.neosoft.neostore.models.AddressModel

@Dao
interface AddressDao {
    @Insert
    fun insert(addressModel: AddressModel)

}