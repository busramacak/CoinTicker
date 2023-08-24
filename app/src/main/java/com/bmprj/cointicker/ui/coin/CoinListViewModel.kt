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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val coinsUseCase: GetCoinsUseCase,
    private val coinDAO: CoinDAO
) :ViewModel() {

    val coins = MutableLiveData<UiState<CoinEntity>>()

    val filteredCoins = MutableLiveData<ArrayList<Entity>>() // Filtrelenmiş sonuçlar


    fun getData() =  viewModelScope.launch{

        coinsUseCase.getCoins()
            .onStart {
                coins.value=UiState.Loading
            }
            .catch {
                coins.value=UiState.Error(it)
            }
            .collect{
                coins.value=it
            }
    }

    fun insertCoins(list : ArrayList<CoinMarketItem>) =  viewModelScope.launch {

        coinDAO.insertAllCoins(marketItemToEntity(list))
    }

    private fun marketItemToEntity(marketItemList:ArrayList<CoinMarketItem>):List<Entity>{
        return marketItemList.map {
            Entity(
                it.currentPrice,
                it.id,
                it.image,
                it.name,
                it.symbol)
        }
    }

    fun getDataFromDatabase(query:String) =  viewModelScope.launch {

        val aList = ArrayList<Entity>(coinDAO.getCoin(query))
        filteredCoins.value=aList
    }

    fun logOut(){
        authRepository.logout()
    }
}