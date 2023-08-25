package com.bmprj.cointicker.data.remote.firebase.auth

import com.bmprj.cointicker.utils.ApiResources
import com.bmprj.cointicker.utils.FirebaseAuthResources
import com.bmprj.cointicker.utils.NetworkManager
import com.bmprj.cointicker.utils.handleAuthResult
import com.bmprj.cointicker.utils.handleResponse
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth:FirebaseAuth,
    private val networkManager: NetworkManager
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Flow<FirebaseAuthResources<AuthResult>> =flow{
        val response=firebaseAuth.signInWithEmailAndPassword(email,password).await()
        val isNetworkAvailable = networkManager.checkNetworkAvailable()
        val isSuccess = response.user!=null
        val result = handleAuthResult(isSuccess,response,isNetworkAvailable)
        emit(result)
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String,
    ): Flow<FirebaseUser> =flow {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
        emit(result.user!!)
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}