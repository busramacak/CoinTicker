package com.bmprj.cointicker.di

import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepositoryImpl
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepository
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepositoryImpl
import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepository
import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class FirebaseModule {
    companion object{
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
        @Provides
        @Singleton
        fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
        @Provides
        @Singleton
        fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()
    }

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