package com.bmprj.cointicker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.coin.CoinUtils
import com.bmprj.cointicker.data.db.CoinDAO
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepository
import com.bmprj.cointicker.model.CoinMarketItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val apiUtils: CoinUtils,
    private val coinDAO: CoinDAO
) :ViewModel() {

    val coins = MutableLiveData<ArrayList<CoinMarketItem>>()

    val filteredCoins = MutableLiveData<ArrayList<Entity>>() // Filtrelenmiş sonuçlar


    val currentUser = repository.currentUser



    fun getData(){
        viewModelScope.launch{
            val r=apiUtils.getCoins().body()

            val list = ArrayList<CoinMarketItem>()

            for(i in 0 until r?.size!!){
                val v = r[i]

                val c = CoinMarketItem(
                    v.currentPrice,
                    v.high24h,
                    v.id,v.image,
                    v.lastUpdated,
                    v.low24h,v.name,
                    v.priceChange24h,
                    v.priceChangePercentage24h,v.symbol)
                list.add(c)
            }

            coins.value=list
        }
    }

    fun insertCoins(list : ArrayList<CoinMarketItem>){
        viewModelScope.launch {
            coinDAO.insertAllCoins(marketItemToEntity(list))
        }
    }

    private fun marketItemToEntity(marketItemList:ArrayList<CoinMarketItem>):List<Entity>{
        return marketItemList.map {
            Entity(
                it.currentPrice,
                it.high24h,
                it.id,
                it.image,
                it.lastUpdated,
                it.low24h,
                it.name,
                it.priceChange24h,
                it.priceChangePercentage24h,
                it.symbol)
        }
    }

    fun getDataFromDatabase(query:String){

        viewModelScope.launch {
           val aList = ArrayList<Entity>(coinDAO.getCoin(query))
            filteredCoins.value=aList
        }
    }

    fun logOut(){
        repository.logout()
    }
}