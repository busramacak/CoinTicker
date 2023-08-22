package com.bmprj.cointicker.data.remote.coin

import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.model.CoinMarketItem
import kotlinx.coroutines.flow.Flow

interface CoinRepository {

    suspend fun getCoin(id:String):Flow<CoinDetail>

    suspend fun getCoins(): Flow<List<CoinMarketItem>>
}