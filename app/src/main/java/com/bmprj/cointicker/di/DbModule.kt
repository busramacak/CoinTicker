package com.bmprj.cointicker.di

import android.content.Context
import androidx.room.Room
import com.bmprj.cointicker.data.db.CoinDAO
import com.bmprj.cointicker.data.db.CoinDatabase
import com.bmprj.cointicker.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DbModule {

    @Provides
    @ViewModelScoped
    fun provideDatabase(@ApplicationContext context: Context): CoinDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CoinDatabase::class.java,
                Constants.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    @ViewModelScoped
    fun provideDAO(db: CoinDatabase) : CoinDAO {
        return db.coinDAO()
    }
}