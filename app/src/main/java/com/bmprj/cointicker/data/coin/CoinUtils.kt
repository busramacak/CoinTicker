package com.bmprj.cointicker.data.coin

import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.model.CoinMarketItem
import retrofit2.Response
import javax.inject.Inject

class CoinUtils @Inject constructor(private val api : CoinApiService){

    suspend fun getCoins() : Response<List<CoinMarketItem>> {
        return api.getCoins()

    }

    suspend fun getCoin(id:String) : Response<CoinDetail>{
        return  api.getCoin(id)
    }
}