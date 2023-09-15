package com.bmprj.cointicker.data.remote.coin

import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.model.CoinListResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// Data provided by CoinGecko ->> www.coingecko.com/en/api
interface CoinApiService {
    @GET("coins/markets?vs_currency=usd")
    suspend fun getCoins(): Response<CoinListResponseModel>

    @GET("coins/{id}")
    suspend fun getCoin(@Path("id") id: String): Response<CoinDetail>
}