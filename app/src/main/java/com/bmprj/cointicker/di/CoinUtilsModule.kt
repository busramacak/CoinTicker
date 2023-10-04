package com.bmprj.cointicker.di

import com.bmprj.cointicker.BuildConfig
import com.bmprj.cointicker.data.remote.coin.CoinApiService
import com.bmprj.cointicker.data.remote.coin.CoinRepository
import com.bmprj.cointicker.data.remote.coin.CoinRepositoryImpl
import com.bmprj.cointicker.utils.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(ViewModelComponent::class)
@Module
object CoinUtilsModule {

    @Provides
    @ViewModelScoped
    fun provideCoinUtils(
        api: CoinApiService, networkManager: NetworkManager,
    ): CoinRepository = CoinRepositoryImpl(api, networkManager)

    @Provides
    @ViewModelScoped
    fun provideCoinApiService(): CoinApiService {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level=HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder()


        if(BuildConfig.DEBUG){ //debug modda mı değil mi ?
            client.addInterceptor(loggingInterceptor) //loglama için debug moddaysa interceptor eklesin.
        }

        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build()) //httpclient
            .build()
            .create(CoinApiService::class.java)
    }
}