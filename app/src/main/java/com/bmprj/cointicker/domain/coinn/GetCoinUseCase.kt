package com.bmprj.cointicker.domain.coins

import com.bmprj.cointicker.data.remote.coin.CoinRepository
import com.bmprj.cointicker.utils.ApiResources
import com.bmprj.cointicker.utils.RetrofitError
import com.bmprj.cointicker.utils.UiState
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(private val coinRepository: CoinRepository) {

    suspend fun getCoin(id:String): Flow<UiState<CoinDetailEntity>> {
       return flow<UiState<CoinDetailEntity>> {
            coinRepository.getCoin(id,)
                .onStart {
                    emit(UiState.Loading)
                }
                .catch {
                    emit(UiState.Error(it))
                }
                .collect {
                    when (it) {
                        is ApiResources.Success -> {
                            val coinEntity = it.result.asCoinDetailEntity()
                            emit(UiState.Success(coinEntity))
                        }

                        is ApiResources.Failure -> {
                            val uiStateError = when (it.exception) {
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
                    }
                }
        }
    }

}