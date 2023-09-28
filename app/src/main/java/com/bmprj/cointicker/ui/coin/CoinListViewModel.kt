package com.bmprj.cointicker.ui.coin

import android.app.Application
import android.net.Uri
import com.bmprj.cointicker.base.BaseViewModel
import com.bmprj.cointicker.data.db.CoinDAO
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepository
import com.bmprj.cointicker.domain.auth.GetAuthUseCase
import com.bmprj.cointicker.domain.coin.CoinEntity
import com.bmprj.cointicker.domain.coin.CoinMarketItemEntity
import com.bmprj.cointicker.domain.coin.GetCoinsUseCase
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.utils.CustomSharedPreference
import com.bmprj.cointicker.utils.FirebaseAuthResources
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.annotation.Nullable
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    application: Application,
    private val customPreference: CustomSharedPreference,
    private val coinsUseCase: GetCoinsUseCase,
    private val coinDAO: CoinDAO,
    private val authUseCase: GetAuthUseCase,
    private val storageRepository: StorageRepository,
    @Nullable val firebaseUser: FirebaseUser?,
) : BaseViewModel(application) {

    private val refreshTime = 10* 1000 * 1000 * 1000L //atıl samancı -> 10dk nanosaniye

    private val _coins = MutableStateFlow<UiState<CoinEntity>>(UiState.Loading)
    val coins = _coins.asStateFlow()

    private val _filteredCoins = MutableStateFlow<ArrayList<Entity>>(arrayListOf())
    val filteredCoins = _filteredCoins.asStateFlow()

    private val _userInfo = MutableStateFlow<UiState<Uri>>(UiState.Loading)
    val userInfo = _userInfo.asStateFlow()

    private val _logOut = MutableStateFlow<UiState<FirebaseAuthResources<Unit>>>(UiState.Loading)
    val logOut = _logOut.asStateFlow()

    fun getUserInfo() = launch {
        if (firebaseUser == null) return@launch
        storageRepository.getPhoto(firebaseUser.uid).customEmit(_userInfo)
    }

    fun getDataFromDatabase(query: String) = launch {
        val aList = ArrayList<Entity>(coinDAO.getCoin(query))
        _filteredCoins.emit(aList)
    }

    fun getData() = launch {// todo (i hope was succes) add to cache mecahism
        val updateTime = customPreference.getTime()

        if (updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            // 10 dakikadan az zamanda getData çağırılırsa database içerisinden al.
            getDataFromDatabase()
        } else {
            customPreference.saveTime(System.nanoTime())
            coinsUseCase.getCoins().customEmit(_coins)
        }
    }

    fun logOut() = launch {
        authUseCase.logout().customEmit(_logOut)
    }

    fun insertCoins(list: ArrayList<CoinMarketItem>) = launch {
        coinDAO.insertAllCoins(marketItemToEntity(list))
    }

    private fun getDataFromDatabase() = launch {
        val entityList = ArrayList<Entity>(coinDAO.getCoins())
        val coinMarketItemEntityList = entityList.map {
            CoinMarketItemEntity(
                it.currentPrice, it.high24h, it.id, it.image, it.lastUpdated,
                it.low24h, it.name, it.priceChange24h, it.priceChangePercentage24h, it.symbol
            )
        }
        val coinEntity = CoinEntity()
        coinEntity.addAll(coinMarketItemEntityList)
        _coins.emit(UiState.Success(coinEntity))
    }

    private fun marketItemToEntity(marketItemList: ArrayList<CoinMarketItem>): List<Entity> {
        return marketItemList.map {
            Entity(
                it.currentPrice, it.id, it.image, it.name, it.symbol, it.priceChange24h,
                it.priceChangePercentage24h, it.currentPrice, it.lastUpdated!!, it.low24h
            )
        }
    }
}