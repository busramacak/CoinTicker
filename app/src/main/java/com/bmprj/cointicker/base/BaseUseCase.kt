package com.bmprj.cointicker.base

import com.bmprj.cointicker.utils.ApiResources
import com.bmprj.cointicker.utils.RetrofitError
import com.bmprj.cointicker.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

//                    T:CoinDetail
//                    R:repository
//                    RE:CoinDetailEntity
abstract class BaseUseCase<T:Any,R:Any,RE:Any> {

    abstract val repository: R
    abstract fun mapSuccess(result: T): RE

    suspend fun Flow<ApiResources<T>>.dumbEmit():Flow<UiState<RE>>{
        return flow {
            this@dumbEmit
                .onStart {
                    emit(UiState.Loading)
                }
                .catch {
                    emit(UiState.Error(it))
                }
                .collect{
                    when(it){
                        is ApiResources.Failure -> {
                            val uiStateError = when(it.exception){
                                is RetrofitError.NoInternetError -> {
                                    UiState.Error(Throwable("İnternetiniz kontrol edin"))
                                }
                                is RetrofitError.ServerError<*> -> {
                                    UiState.Error(Throwable("Kısa bir onarım sürecindeyiz anlayşınız için teşekkür ederiz"))
                                }
                                is RetrofitError.UnKnown -> {
                                    UiState.Error(Throwable("Beklenmedik bir hata oluştu lütfen tekrar deneyiniz"))
                                }
                            }

                            emit(uiStateError)
                        }
                        is ApiResources.Success -> {
                            println(it)
                            val mappedResult =mapSuccess(it.result)
                            println(mappedResult)
                            emit(UiState.Success(mappedResult))
                        }
                    }
                }
        }

    }



}