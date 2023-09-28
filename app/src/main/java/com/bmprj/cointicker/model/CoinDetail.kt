package com.bmprj.cointicker.model


import com.google.gson.annotations.SerializedName

//builder design pattern -> kullanılacak parçaların gereksiz yere kaynak harcamasını önler.
// nullable değişkenler olsaydı builder design pattern diyebilirdik.
data class CoinDetail(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: Image,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("market_data")
    val marketData: MarketData,
    @SerializedName("description")
    val description: Description,
    @SerializedName("symbol")
    val symbol: String,
)