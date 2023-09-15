package com.bmprj.cointicker.data.remote.firebase.auth

import com.bmprj.cointicker.utils.FirebaseAuthResources
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow


interface AuthRepository {
    suspend fun login(email:String, password:String): Flow<FirebaseAuthResources<AuthResult>>
    suspend fun signup(name:String,email:String,password: String): Flow<FirebaseAuthResources<AuthResult>>
    fun logout(): Flow<FirebaseAuthResources<Unit>>
}