package com.bmprj.cointicker.data

import com.bmprj.cointicker.model.CoinMarketItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class CoinUtils @Inject constructor(private val api :CoinApiService){

    suspend fun getCoins() : Response<List<CoinMarketItem>> {
        return api.getCoins()

    }
}