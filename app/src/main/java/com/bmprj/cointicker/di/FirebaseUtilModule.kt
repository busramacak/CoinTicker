package com.bmprj.cointicker.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.annotation.Nullable

@InstallIn(ViewModelComponent::class)
@Module
 class FirebaseUtilModule {
    @Provides
    @Nullable
    fun provideFirebaseCurrentUser(firebaseAuth: FirebaseAuth) : FirebaseUser? = firebaseAuth.currentUser
}