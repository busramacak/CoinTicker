package com.bmprj.cointicker.model


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName


data class Description(
    @SerializedName("en")
    val en: String,
    @SerializedName("tr")
    val tr:String
)