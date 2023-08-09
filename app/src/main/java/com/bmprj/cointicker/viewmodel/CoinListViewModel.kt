package com.bmprj.cointicker.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.CoinUtils
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val apiUtils:CoinUtils) :ViewModel() {

    val coins = MutableLiveData<ArrayList<CoinMarketItem>>()



    fun getData(){
        viewModelScope.launch{
            val r=apiUtils.getCoins().body()
            println(apiUtils.getCoins().errorBody())
            val list = ArrayList<CoinMarketItem>()

            for(i in 0 until r?.size!!){
                val v = r.get(i)
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