package com.bmprj.cointicker.di

import com.bmprj.cointicker.data.remote.coin.CoinApiService
import com.bmprj.cointicker.data.remote.coin.CoinRepository
import com.bmprj.cointicker.data.remote.coin.CoinRepositoryImpl
import com.bmprj.cointicker.utils.Constants
import com.bmprj.cointicker.utils.NetworkManager
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
    fun provideCoinUtils(
        api: CoinApiService, networkManager: NetworkManager,
    ): CoinRepository = CoinRepositoryImpl(api, networkManager)

    @Provides
    @Singleton
    fun provideCoinApiService(): CoinApiService {
        val BASE_URL = Constants.BASE_URL

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(CoinApiService::class.java)
    }


}