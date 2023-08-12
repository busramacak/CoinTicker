package com.bmprj.cointicker.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Low24h(

    @SerialName("usd")
    val usd: Int
)