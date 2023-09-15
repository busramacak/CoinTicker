package com.bmprj.cointicker.di

import android.content.Context
import com.bmprj.cointicker.utils.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideNetowrkManager(@ApplicationContext context: Context): NetworkManager {
        return NetworkManager(context)
    }
}