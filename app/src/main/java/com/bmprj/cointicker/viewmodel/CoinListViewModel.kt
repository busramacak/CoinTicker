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



    fun getData(vs_currency:String){
        viewModelScope.launch{
            val t=apiUtils.getCoins(vs_currency).body()?.get(0)!!
            println(t)

//            val list = ArrayList<CoinMarketItem>()
//            val c = CoinMarketItem(
//                t.currentPrice,
//                t.high24h,
//                t.id,t.image,
//                t.lastUpdated,
//                t.low24h,t.name,
//                t.priceChange24h,
//                t.priceChangePercentage24h,t.symbol)
//            list.add(c)
//
//            coins.value=list
        }
    }
}