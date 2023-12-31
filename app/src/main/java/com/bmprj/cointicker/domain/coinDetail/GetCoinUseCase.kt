package com.bmprj.cointicker.domain.coinList

import com.bmprj.cointicker.base.BaseUseCase
import com.bmprj.cointicker.data.remote.coin.CoinRepository
import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.utils.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    override val repository: CoinRepository,
) : BaseUseCase<CoinDetail, CoinRepository, CoinDetailEntity>() {

    override fun mapSuccess(result: CoinDetail): CoinDetailEntity {
        return result.asCoinDetailEntity()
    }

    suspend fun getCoin(id: String): Flow<UiState<CoinDetailEntity>> {

        return repository.getCoin(id).dumbEmit()

    }
}
