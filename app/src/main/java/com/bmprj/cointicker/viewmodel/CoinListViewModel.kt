package com.bmprj.cointicker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.coin.CoinUtils
import com.bmprj.cointicker.data.db.CoinDAO
import com.bmprj.cointicker.model.CoinMarketItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val apiUtils: CoinUtils,
    private val coinDAO: CoinDAO
) :ViewModel() {

    val coins = MutableLiveData<ArrayList<CoinMarketItem>>()



    fun getData(){
        viewModelScope.launch{
            val r=apiUtils.getCoins().body()
            println(apiUtils.getCoins().errorBody())
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
}