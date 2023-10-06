package com.bmprj.cointicker.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object FirebaseModule {
    @Provides
    @ViewModelScoped
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    @Provides
    @ViewModelScoped
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    @Provides
    @ViewModelScoped
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()
}