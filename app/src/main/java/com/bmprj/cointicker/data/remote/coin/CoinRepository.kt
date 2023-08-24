package com.bmprj.cointicker.data.remote.coin

import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.model.CoinListResponseModel
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.utils.ApiResources
import kotlinx.coroutines.flow.Flow

interface CoinRepository {

    suspend fun getCoin(id:String): Flow<ApiResources<CoinDetail>>

    suspend fun getCoins(): Flow<ApiResources<CoinListResponseModel>>
}