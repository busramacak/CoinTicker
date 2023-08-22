package com.bmprj.cointicker.data.remote.firebase.auth

import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth:FirebaseAuth
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Flow<FirebaseUser> =flow{

        emit(firebaseAuth.signInWithEmailAndPassword(email,password).result.user!!)
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String,
    ): Flow<FirebaseUser> =flow {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password)
        result.result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
        emit(result.result.user!!)
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}