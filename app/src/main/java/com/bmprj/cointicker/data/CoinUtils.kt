package com.bmprj.cointicker.data

import com.bmprj.cointicker.model.CoinMarketItem
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class CoinUtils @Inject constructor(private val api :CoinApiService){

//    companion object{

//        private val BASE_URL ="https://api.coingecko.com/api/v3"

//        private val api = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(CoinApiService::class.java)
//    }

    suspend fun getCoins(vs_currency:String) : Response<List<CoinMarketItem>> {
        return api.getCoins(vs_currency)

    }
}