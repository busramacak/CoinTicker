package com.bmprj.cointicker.model


import kotlinx.serialization.SerialName


data class Description(
    @SerialName("en")
    val en: String
)