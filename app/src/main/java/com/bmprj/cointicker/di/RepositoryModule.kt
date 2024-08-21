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
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
interface RepositoryModule {
    @Binds
    @ViewModelScoped
    fun bindAuthRepo(impl:AuthRepositoryImpl): AuthRepository
    @Binds
    @ViewModelScoped
    fun bindFirestoreRepo(impl: CloudRepositoryImpl): CloudRepository
    @Binds
    @ViewModelScoped
    fun bindStorageRepo(impl: StorageRepositoryImpl): StorageRepository
}