package com.bmprj.cointicker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bmprj.cointicker.model.CoinMarketItem

@Database(entities = [CoinMarketItem::class], version = 1)
abstract class CoinDatabase :RoomDatabase() {
    abstract fun coinDAO(): CoinDAO
}