package com.bmprj.cointicker.data.remote.coin

import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.model.CoinMarketItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api : CoinApiService
) : CoinRepository{

    override suspend fun getCoins() : Flow<List<CoinMarketItem>> = flow {
        emit(api.getCoins())
    }

    override suspend fun getCoin(id:String) : Flow<CoinDetail> = flow{
        emit(api.getCoin(id))
    }
}