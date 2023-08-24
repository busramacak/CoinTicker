package com.bmprj.cointicker.domain.coin

import com.bmprj.cointicker.model.CoinListResponseModel
import com.bmprj.cointicker.model.CoinMarketItem


// CoinListResponseModel mapping to CoinEntity
fun CoinListResponseModel.asCoinEntity(): CoinEntity {
    val list = this.map {
        CoinMarketItemEntity(
            it.currentPrice,
            it.high24h,
            it.id,
            it.image,
            it.lastUpdated,
            it.low24h,
            it.name,
            it.priceChange24h,
            it.priceChangePercentage24h,
            it.symbol
        )
    }
    val coinEntity = CoinEntity()
    coinEntity.addAll(list)
    return coinEntity
}

fun CoinEntity.asList(): List<CoinMarketItem>  = this.toList().map {
    CoinMarketItem(
        it.currentPrice,it.high24h,it.id,it.image,it.lastUpdated,it.low24h,it.name,it.priceChange24h,it.priceChangePercentage24h,
        it.symbol
    )
}