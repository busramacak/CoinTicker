package com.bmprj.cointicker.model


import com.google.gson.annotations.SerializedName


data class CurrentPrice(
    @SerializedName("usd")
    val usd: Double,
)