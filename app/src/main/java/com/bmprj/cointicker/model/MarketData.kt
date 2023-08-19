package com.bmprj.cointicker.model


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class MarketData(


    @SerializedName("current_price")
    val currentPrice: CurrentPrice,

    @SerializedName("high_24h")
    val high24h: High24h,

    @SerializedName("last_updated")
    val lastUpdated: String,

    @SerializedName("low_24h")
    val low24h: Low24h,

    @SerializedName("price_change_24h")
    val priceChange24h: Double,

    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,

)