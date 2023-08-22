package com.bmprj.cointicker.di

import android.content.Context
import androidx.room.Room
import com.bmprj.cointicker.data.db.CoinDAO
import com.bmprj.cointicker.data.db.CoinDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CoinDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CoinDatabase::class.java,
                "coin")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideDAO(db: CoinDatabase) : CoinDAO {
        return db.coinDAO()
    }
}