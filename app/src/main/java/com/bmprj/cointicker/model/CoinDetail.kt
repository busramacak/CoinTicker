package com.bmprj.cointicker.model


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class CoinDetail(


    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("image")
    val image: İmage,

    @SerializedName("last_updated")
    val lastUpdated: String,

    @SerializedName("market_data")
    val marketData: MarketData,

    @SerializedName("description")
    val description: Description,

    @SerializedName("symbol")
    val symbol: String,
)