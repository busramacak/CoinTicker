package com.bmprj.cointicker.data

import com.bmprj.cointicker.model.CoinMarketItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinApiService {

    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") vs_currency:String
    ) : Response<List<CoinMarketItem>>
}