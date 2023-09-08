package com.bmprj.cointicker.ui.coin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.coin.CoinRepositoryImpl
import com.bmprj.cointicker.data.db.CoinDAO
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.domain.coin.CoinEntity
import com.bmprj.cointicker.domain.coin.GetCoinsUseCase
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val coinsUseCase: GetCoinsUseCase,
    private val coinDAO: CoinDAO
) :ViewModel() {

    private val _coins = MutableStateFlow<UiState<CoinEntity>>(UiState.Loading)
    val coins = _coins.asStateFlow()

    fun getData() =  viewModelScope.launch{
        coinsUseCase.getCoins()
            .onStart {
                _coins.emit(UiState.Loading)
            }
            .catch {
                _coins.emit(UiState.Error(it))
            }
            .collect{
                _coins.emit(it)
            }
    }

    fun insertCoins(list : ArrayList<CoinMarketItem>) =  viewModelScope.launch {
        coinDAO.insertAllCoins(marketItemToEntity(list))
    }

    private fun marketItemToEntity(marketItemList:ArrayList<CoinMarketItem>):List<Entity>{
        return marketItemList.map {
            Entity(it.currentPrice, it.id, it.image, it.name, it.symbol)
        }
    }
}