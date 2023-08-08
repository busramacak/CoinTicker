package com.bmprj.cointicker.di

import com.bmprj.cointicker.data.auth.AuthRepository
import com.bmprj.cointicker.data.auth.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth():FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepo(impl: AuthRepositoryImpl): AuthRepository = impl
}