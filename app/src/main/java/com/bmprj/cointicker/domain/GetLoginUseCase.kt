package com.bmprj.cointicker.domain

import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.utils.FirebaseAuthError
import com.bmprj.cointicker.utils.FirebaseAuthResources
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    suspend fun login(email:String,password:String): Flow<UiState<FirebaseUser>> =flow{

        authRepository.login(email, password)
            .onStart {
                emit(UiState.Loading)
            }.catch {
                emit(UiState.Error(it))
            }
            .collect{
                when(it){
                    is FirebaseAuthResources.Success ->{
                        emit(UiState.Success(it.result.user!!))
                    }
                    is FirebaseAuthResources.Failure ->{
                        val uiStateError = when(it.exception){
                            is FirebaseAuthError.NoInternetError ->{
                                UiState.Error(Throwable("İnternetinizi kontroledin"))
                            }
                            is FirebaseAuthError.ServerError ->{
                                UiState.Error(Throwable("bişiler yanlış gdiyor."))
                            }
                            is FirebaseAuthError.InvalidCredential->
                                UiState.Error(Throwable("email veya şifre hatalı."))
                            is FirebaseAuthError.UnKnown->{
                                UiState.Error(Throwable("Beklenmedik bir hata oluştu"))
                            }
                        }
                        emit(uiStateError)
                    }
                }
            }
    }
}