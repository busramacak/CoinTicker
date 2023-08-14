package com.bmprj.cointicker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

@Entity(tableName = "coin")
data class Entity(
    val currentPrice: Double,
    val high24h: Double,
    @PrimaryKey(autoGenerate = false) val id: String,
    val image: String,
    val lastUpdated: String?,
    val low24h: Double,
    val name: String,
    val priceChange24h: Double,
    val priceChangePercentage24h: Double,
    val symbol: String,
)