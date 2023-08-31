package com.bmprj.cointicker.di

import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepository
import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepositoryImpl
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepositoryImpl
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepository
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepositoryImpl
import com.bmprj.cointicker.utils.NetworkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class FirebaseModule {

    @Provides
    fun provideFirebaseAuth():FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthRepo(firebaseAuth: FirebaseAuth,networkManager: NetworkManager): AuthRepository = AuthRepositoryImpl(firebaseAuth,networkManager)


    @Provides
    fun provideFirebaseFirestore():FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirestoreRepo(impl: CloudRepositoryImpl): CloudRepository =impl


    @Provides
    fun provideFirebaseStorage():FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun provideStorageRepo(impl: StorageRepositoryImpl): StorageRepository =impl
}