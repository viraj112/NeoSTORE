package com.neosoft.neostore.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.neosoft.neostore.models.AddressModel

@Dao
interface AddressDao {

    @Insert
    fun insert(addressModel: AddressModel)

    @Delete
    suspend fun delete(addressModel: AddressModel)

    @Query("select * from addressdata order by id ASC")
    fun getAllAddress():List<AddressModel>
}