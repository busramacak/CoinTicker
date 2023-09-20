package com.bmprj.cointicker.di

import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepositoryImpl
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepository
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepositoryImpl
import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepository
import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class FirebaseModule {  // todo  provideFirebaseModule
    @Binds
    @Singleton
    abstract fun bindAuthRepo(impl:AuthRepositoryImpl): AuthRepository
    @Binds
    @Singleton
    abstract fun bindFirestoreRepo(impl: CloudRepositoryImpl): CloudRepository
    @Binds
    @Singleton
    abstract fun bindStorageRepo(impl: StorageRepositoryImpl): StorageRepository
}