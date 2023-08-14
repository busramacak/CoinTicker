package com.bmprj.cointicker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName

@Entity(tableName = "coin")
@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class CoinMarketItem(
    @SerialName("current_price")
    val currentPrice: Double,
    @SerialName("high_24h")
    val high24h: Double,
    @PrimaryKey(autoGenerate = false)
    @SerialName("id")
    val id: String,
    @SerialName("image")
    val image: String,
    @SerialName("last_updated")
    val lastUpdated: String?,
    @SerialName("low_24h")
    val low24h: Double,
    @SerialName("name")
    val name: String,
    @SerialName("price_change_24h")
    val priceChange24h: Double,
    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,
    @SerialName("symbol")
    val symbol: String,
) : Parcelable

