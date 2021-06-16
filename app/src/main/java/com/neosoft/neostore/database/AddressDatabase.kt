package com.neosoft.neostore.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AddressEntity::class],version = 3)
abstract class AddressDatabase :RoomDatabase(){
    abstract fun addressDao():AddressDao
    }
