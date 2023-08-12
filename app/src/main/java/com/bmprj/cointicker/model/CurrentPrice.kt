package com.bmprj.cointicker.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentPrice(

    @SerialName("usd")
    val usd: Int,
)