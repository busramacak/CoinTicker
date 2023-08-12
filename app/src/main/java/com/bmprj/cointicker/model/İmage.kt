package com.bmprj.cointicker.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class İmage(
    @SerialName("large")
    val large: String,
    @SerialName("small")
    val small: String,
    @SerialName("thumb")
    val thumb: String
)