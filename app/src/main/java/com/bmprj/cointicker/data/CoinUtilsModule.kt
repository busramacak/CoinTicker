package com.bmprj.cointicker.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoinUtilsModule {

    @Provides
    @Singleton
    fun provideCoinUtils(api:CoinApiService) : CoinUtils{
        return CoinUtils(api)
    }

    @Provides
    @Singleton
    fun provideCoinApiService():CoinApiService{
        val BASE_URL ="https://api.coingecko.com/api/v3/"

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinApiService::class.java)
    }


}