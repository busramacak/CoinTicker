package com.bmprj.cointicker.model


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Low24h(

    @SerializedName("usd")
    val usd: Int
)