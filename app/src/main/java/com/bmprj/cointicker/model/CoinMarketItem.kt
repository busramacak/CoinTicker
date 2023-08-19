package com.bmprj.cointicker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.gson.annotations.SerializedName

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class CoinMarketItem(
    @SerializedName("current_price")
    val currentPrice: Double,
    @SerializedName("high_24h")
    val high24h: Double,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("last_updated")
    val lastUpdated: String?,
    @SerializedName("low_24h")
    val low24h: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("price_change_24h")
    val priceChange24h: Double,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,
    @SerializedName("symbol")
    val symbol: String,
) : Parcelable

