package com.bmprj.cointicker.domain.coinList

import com.bmprj.cointicker.model.CoinDetail

fun CoinDetail.asCoinDetailEntity(): CoinDetailEntity {
    return CoinDetailEntity(id, name, image, lastUpdated, marketData, description, symbol)
}

fun CoinDetailEntity.asCoinDetail(): CoinDetail {
    return CoinDetail(id, name, image, lastUpdated, marketData, description, symbol)
}