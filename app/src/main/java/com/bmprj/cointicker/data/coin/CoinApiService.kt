package com.bmprj.cointicker.data.coin

import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.model.CurrentPrice
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Data provided by CoinGecko ->> www.coingecko.com/en/api
interface CoinApiService {

    @GET("coins/markets?vs_currency=usd")
    suspend fun getCoins() : Response<List<CoinMarketItem>>

    @GET("coins/")
    suspend fun getCoin(
        @Query("id") id:String
    ) : Response<List<CoinDetail>>
}