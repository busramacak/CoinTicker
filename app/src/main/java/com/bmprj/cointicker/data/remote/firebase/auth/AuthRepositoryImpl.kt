package com.bmprj.cointicker.data.remote.firebase.auth

import com.bmprj.cointicker.utils.FirebaseAuthResources
import com.bmprj.cointicker.utils.NetworkManager
import com.bmprj.cointicker.utils.handleAuthResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth:FirebaseAuth,
    private val networkManager: NetworkManager
) : AuthRepository {

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
    ): Flow<FirebaseAuthResources<AuthResult>> =flow {
        val response = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val isNetworkAvailable = networkManager.checkNetworkAvailable()
        val isSuccess = response.user!=null
        val result = handleAuthResult(isSuccess,response,isNetworkAvailable)
        response.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
        emit(result)
    }

    override fun logout():Flow<FirebaseAuthResources<Unit>> = flow{
        val response = firebaseAuth.signOut()

        emit(FirebaseAuthResources.Success(response))
    }
}