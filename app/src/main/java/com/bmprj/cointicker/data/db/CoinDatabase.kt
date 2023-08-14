package com.bmprj.cointicker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Entity::class], version = 2)
abstract class CoinDatabase :RoomDatabase() {
    abstract fun coinDAO(): CoinDAO
}