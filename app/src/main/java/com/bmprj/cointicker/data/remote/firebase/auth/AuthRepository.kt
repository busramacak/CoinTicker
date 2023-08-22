package com.bmprj.cointicker.data.remote.firebase.auth

import com.bmprj.cointicker.utils.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow


interface AuthRepository {
    val currentUser : FirebaseUser?
    suspend fun login(email:String, password:String): Flow<FirebaseUser>
    suspend fun signup(name:String,email:String,password: String): Flow<FirebaseUser>
    fun logout()
}