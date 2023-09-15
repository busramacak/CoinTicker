package com.bmprj.cointicker.data.remote.coin

import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.model.CoinListResponseModel
import com.bmprj.cointicker.utils.ApiResources
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    /** Returns the details of the cryptocurrency with the specified [id] as a [Flow] containing [ApiResources]. */
    suspend fun getCoin(id: String): Flow<ApiResources<CoinDetail>>

    /** Returns the current list of cryptocurrencies as a [Flow] containing [ApiResources]. */
    suspend fun getCoins(): Flow<ApiResources<CoinListResponseModel>>
}