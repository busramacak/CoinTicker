package com.bmprj.cointicker.domain.coins

import com.bmprj.cointicker.model.Description
import com.bmprj.cointicker.model.MarketData
import com.bmprj.cointicker.model.Image

data class CoinDetailEntity(
    val id: String,
    val name: String,
    val image: Image,
    val lastUpdated: String,
    val marketData: MarketData,
    val description: Description,
    val symbol: String,
)