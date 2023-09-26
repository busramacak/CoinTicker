package com.bmprj.cointicker.di

import com.bmprj.cointicker.BuildConfig
import com.bmprj.cointicker.data.remote.coin.CoinApiService
import com.bmprj.cointicker.data.remote.coin.CoinRepository
import com.bmprj.cointicker.data.remote.coin.CoinRepositoryImpl
import com.bmprj.cointicker.utils.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        val BASE_URL = BuildConfig.BASE_URL

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level=HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder()


        if(BuildConfig.DEBUG){ //debug modda mı değil mi ?
            client.addInterceptor(loggingInterceptor) //loglama için debug moddaysa interceptor eklesin.
        }

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build()) //httpclienti ekledik
            .build()
            .create(CoinApiService::class.java)



        //  todo (oldu galiba) debug modda http isteklerini logda görelim relase de görmeyelim
    }


}