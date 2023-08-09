package com.bmprj.cointicker.data

import com.bmprj.cointicker.model.CoinMarketItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Data provided by CoinGecko ->> www.coingecko.com/en/api
interface CoinApiService {

    @GET("coins/markets?vs_currency=usd")
    suspend fun getCoins() : Response<List<CoinMarketItem>>
}