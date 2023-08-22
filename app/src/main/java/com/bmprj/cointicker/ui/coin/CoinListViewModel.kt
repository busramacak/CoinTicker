package com.bmprj.cointicker.ui.coin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.coin.CoinRepositoryImpl
import com.bmprj.cointicker.data.db.CoinDAO
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.model.CoinMarketItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val coinRepository: CoinRepositoryImpl,
    private val coinDAO: CoinDAO
) :ViewModel() {

    val coins = MutableLiveData<ArrayList<CoinMarketItem>>()

    val filteredCoins = MutableLiveData<ArrayList<Entity>>() // Filtrelenmiş sonuçlar


    fun getData() =  viewModelScope.launch{

        coinRepository.getCoins()
            .catch {
                it.printStackTrace()
            }
            .collect{
                coins.value=ArrayList(it)
            }

//                val list = ArrayList<CoinMarketItem>()
//
//                for(i in 0 until r?.size!!){
//                    val v = r[i]
//
//                    val c = CoinMarketItem(
//                        v.currentPrice,
//                        v.high24h,
//                        v.id,v.image,
//                        v.lastUpdated,
//                        v.low24h,v.name,
//                        v.priceChange24h,
//                        v.priceChangePercentage24h,v.symbol)
//                    list.add(c)
//                }
//                println(list)
//
//                coins.value=list

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