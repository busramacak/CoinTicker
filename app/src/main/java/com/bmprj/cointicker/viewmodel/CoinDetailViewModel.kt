package com.bmprj.cointicker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.coin.CoinUtils
import com.bmprj.cointicker.model.CoinDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val apiUtil: CoinUtils
) :ViewModel(){

    val coinDetail = MutableLiveData<CoinDetail?>()

    fun getCoin(id:String){
        viewModelScope.launch {

//            CoinDetail(id=bitcoin, name=Bitcoin, image=İmage(large=https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579, small=https://assets.coingecko.com/coins/images/1/small/bitcoin.png?1547033579, thumb=https://assets.coingecko.com/coins/images/1/thumb/bitcoin.png?1547033579), lastUpdated=null, marketData=null, description=null, symbol=btc)
//                2023-08-11 16:07:27.431 19254-19254
            val r = apiUtil.getCoin(id).body()?.get(0)


//            val list = ArrayList<CoinDetail>()
//            val c = CoinDetail(r?.id!!,r.name,r.image,r.lastUpdated,r.marketData,r.description,r.symbol)
//            list.add(c)
            coinDetail.value=r
        }

    }
}