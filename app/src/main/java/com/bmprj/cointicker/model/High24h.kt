package com.bmprj.cointicker.model


import com.google.gson.annotations.SerializedName


data class High24h(
    @SerializedName("usd")
    val usd: Double,
)