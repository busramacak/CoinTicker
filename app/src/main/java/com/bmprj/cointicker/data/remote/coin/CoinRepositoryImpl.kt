package com.bmprj.cointicker.data.remote.coin

import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.model.CoinListResponseModel
import com.bmprj.cointicker.utils.ApiResources
import com.bmprj.cointicker.utils.NetworkManager
import com.bmprj.cointicker.utils.handleResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val api: CoinApiService,
    private val networkManager: NetworkManager,
) : CoinRepository {

    override suspend fun getCoins(): Flow<ApiResources<CoinListResponseModel>> = flow {
        val response = api.getCoins()
        val isNetworkAvailable = networkManager.checkNetworkAvailable()
        val isSuccessful = response.isSuccessful
        val result = handleResponse(isSuccessful, response, isNetworkAvailable)
        emit(result)
    }

    override suspend fun getCoin(id: String): Flow<ApiResources<CoinDetail>> = flow {
        val response = api.getCoin(id)
        val isNetworkAvailable = networkManager.checkNetworkAvailable()
        val isSuccessful = response.isSuccessful
        val result = handleResponse(isSuccessful, response, isNetworkAvailable)
        emit(result)
    }
}