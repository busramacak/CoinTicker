package com.bmprj.cointicker.domain.coin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


class CoinEntity : ArrayList<CoinMarketItemEntity>()

@Parcelize
data class CoinMarketItemEntity(
     val currentPrice: Double,
     val high24h: Double,
     val id: String,
     val image: String,
     val lastUpdated: String?,
     val low24h: Double,
     val name: String,
     val priceChange24h: Double,
     val priceChangePercentage24h: Double,
     val symbol: String,
) : Parcelable

