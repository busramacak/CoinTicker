package com.bmprj.cointicker.utils

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import retrofit2.Response


sealed class ApiResources<out R> {
    data class Success<out R>(val result: R) : ApiResources<R>()
    data class Failure(val exception: RetrofitError) : ApiResources<Nothing>()
}

sealed class RetrofitError(val errorMessage: String? = null) {
    data class NoInternetError(val message: String? = null) : RetrofitError(message)
    data class ServerError<out T>(val response: Response<@UnsafeVariance T>) : RetrofitError(handleErrorCode(response))
    class UnKnown() : RetrofitError()
}

private fun <T> handleErrorCode(response: Response<T>): String {
    val errorBody = response.errorBody()
    return when (response.code()) {
        400 -> "Bad Request: $errorBody"
        401 -> "Unauthorized: $errorBody"
        403 -> "Forbidden: $errorBody"
        404 -> "Not Found: $errorBody"
        500 -> "Internal Server Error: $errorBody"
        // ... handle other codes
        else -> "Unknown Error: $errorBody"
    }
}

fun <T> handleResponse(
    isSuccessful: Boolean,
    response: Response<T>,
    isNetworkAvailable: Boolean
): ApiResources<T> {

    val body = response.body()
    if (isSuccessful && response.errorBody() == null && body != null) { // success and have data
        return ApiResources.Success(body)
    }

    val error = if (isSuccessful && response.errorBody() != null) {// success and no data
        RetrofitError.ServerError(response) // handle error code
    } else if (!isNetworkAvailable) { // no success
        RetrofitError.NoInternetError()
    } else {
        RetrofitError.UnKnown()
        // add logs
    }
    return ApiResources.Failure(error)
}

