package com.bmprj.cointicker.domain.coin

import com.bmprj.cointicker.base.BaseUseCase
import com.bmprj.cointicker.data.remote.coin.CoinRepository
import com.bmprj.cointicker.model.CoinListResponseModel
import com.bmprj.cointicker.utils.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(override val repository: CoinRepository) :
    BaseUseCase<CoinListResponseModel, CoinRepository, CoinEntity>() {

    suspend fun getCoins(): Flow<UiState<CoinEntity>> {
        return repository.getCoins().dumbEmit()
    }

    override fun mapSuccess(result: CoinListResponseModel): CoinEntity {
        return result.asCoinEntity()
    }

}