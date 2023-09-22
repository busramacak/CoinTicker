package com.bmprj.cointicker.data.db

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "coin")
data class Entity(
    val high24h: Double,
    @PrimaryKey val id: String,
    val image: String,
    val name: String,
    val symbol: String,
    val priceChange24h: Double,
    val priceChangePercentage24h: Double,
    val currentPrice: Double,
    val lastUpdated:String,
    val low24h:Double
) {

}

