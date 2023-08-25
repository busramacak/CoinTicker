@file:Suppress("IMPLICIT_CAST_TO_ANY")

package com.bmprj.cointicker.utils

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

sealed class FirebaseAuthResources<out R> {
    data class Success<out R>(val result: R) : FirebaseAuthResources<R>()
    data class Failure(val exception: FirebaseAuthError) : FirebaseAuthResources<Nothing>()
}
sealed class FirebaseAuthError(val errorMessage: String? = null) {
    data class NoInternetError(val message: String? = null) : FirebaseAuthError(message)
    data class ServerError(val authResult: AuthResult) : FirebaseAuthError(handleFirebaseAuthErrorCode(authResult))
    data class InvalidCredential(val errorCode: String,val errormessage: String) : FirebaseAuthError(errormessage)

    class UnKnown() : FirebaseAuthError()
}
private fun handleFirebaseAuthErrorCode(authResult: AuthResult): String {
    return when(authResult.additionalUserInfo?.username){
        "user-not-found" -> "kullanıcı bulunamadı veya etkin değil"
        else -> "Bir hata oluştu. Lütfen daha sonra tekrar deneyin"
    }
}


fun handleAuthResult(
    isSuccessful: Boolean,
    authResult: AuthResult,
    isNetworkAvailable: Boolean):FirebaseAuthResources<AuthResult>{

    if(isSuccessful && authResult.user!=null){
        return FirebaseAuthResources.Success(authResult)
    }


    val errorCode = authResult.additionalUserInfo?.providerId

    val error = if (isSuccessful && authResult.user == null) {
        if (errorCode == "password") {
            val errorMessage = handleFirebaseAuthErrorCode(authResult)
            FirebaseAuthError.InvalidCredential(errorCode, errorMessage)
        } else {
            FirebaseAuthError.ServerError(authResult)
        }
    } else if (!isNetworkAvailable) {
        FirebaseAuthError.NoInternetError()
    } else {
        FirebaseAuthError.UnKnown()
    }

    return FirebaseAuthResources.Failure(error)

}