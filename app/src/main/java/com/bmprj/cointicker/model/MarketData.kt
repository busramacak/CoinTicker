package com.bmprj.cointicker.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class MarketData(


    @SerialName("current_price")
    val currentPrice: CurrentPrice,

    @SerialName("high_24h")
    val high24h: High24h,

    @SerialName("last_updated")
    val lastUpdated: String,

    @SerialName("low_24h")
    val low24h: Low24h,

    @SerialName("price_change_24h")
    val priceChange24h: Double,

    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,

)