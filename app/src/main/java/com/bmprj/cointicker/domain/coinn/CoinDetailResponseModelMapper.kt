package com.bmprj.cointicker.domain.coins

import com.bmprj.cointicker.model.CoinDetail

fun CoinDetail.asCoinDetailEntity(): CoinDetailEntity {
    return CoinDetailEntity(id, name, image, lastUpdated, marketData, description, symbol)
}

fun CoinDetailEntity.asCoinDetail(): CoinDetail {
    return CoinDetail(id, name, image, lastUpdated, marketData, description, symbol)
}