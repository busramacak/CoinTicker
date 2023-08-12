package com.bmprj.cointicker.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class CoinDetail(


    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("image")
    val image: İmage,

    @SerialName("last_updated")
    val lastUpdated: String,

    @SerialName("market_data")
    val marketData: MarketData,

    @SerialName("description")
    val description: Description,

    @SerialName("symbol")
    val symbol: String,
)