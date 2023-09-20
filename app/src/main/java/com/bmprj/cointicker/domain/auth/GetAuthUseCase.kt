package com.bmprj.cointicker.domain.auth

import android.net.Uri
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.utils.FirebaseAuthError
import com.bmprj.cointicker.utils.FirebaseAuthResources
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.annotation.Nullable
import javax.inject.Inject

class GetAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    @Nullable private val firebaseUser: FirebaseUser?
) {

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
                        it.result.user?.let {user->
                            emit(UiState.Success(user))
                        }

                    }
                    is FirebaseAuthResources.Failure ->{
                        val uiStateError = when(it.exception){
                            is FirebaseAuthError.NoInternetError ->{
                                UiState.Error(Throwable("İnternetinizi kontrol edin"))
                            }
                            is FirebaseAuthError.ServerError ->{
                                UiState.Error(Throwable("bir şeyler yanlış gidiyor."))
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


    suspend fun signUp(name:String,email: String,password: String):Flow<UiState<FirebaseUser>> = flow{
        authRepository.signup(name, email, password)
            .onStart {
                emit(UiState.Loading)
            }
            .catch {
                emit(UiState.Error(it))
            }
            .collect{
                when(it){
                    is FirebaseAuthResources.Success ->{
                        it.result.user?.let {user->
                            emit(UiState.Success(user))
                        }
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


    suspend fun logout() = flow{
       authRepository.logout()
           .onStart {
               emit(UiState.Loading)
           }.catch {
               emit(UiState.Error(it))
           }.collect{
               emit(UiState.Success(it))
           }
    }

//    suspend fun delete() = flow{
//        if(firebaseUser==null)return@flow
//        println(firebaseUser)
//        emit(firebaseUser.delete().await())
//    }

    suspend fun changeProfileName(name:String) = flow{
        emit(firebaseUser?.updateProfile(userProfileChangeRequest { displayName=name })?.await())
    }

    suspend fun changePassword(newPassword:String) = flow{
        emit(firebaseUser?.updatePassword(newPassword)?.await())
    }

    suspend fun changePhoto(uri: Uri?) = flow{
        emit(firebaseUser?.updateProfile(userProfileChangeRequest { photoUri=uri })?.await())
    }
}