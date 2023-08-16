package com.bmprj.cointicker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.coin.CoinUtils
import com.bmprj.cointicker.data.firebase.cloud.CloudRepository
import com.bmprj.cointicker.data.firebase.di.Resource
import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.model.FavouriteCoin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val apiUtil: CoinUtils,
    private val cloudRepo:CloudRepository
) :ViewModel(){

    val coinDetail = MutableLiveData<CoinDetail?>()

    private val _favouriteAdd = MutableLiveData<Resource<Unit>>()
    val favouriteAdd = _favouriteAdd

    private val _favouriteDelete = MutableLiveData<Resource<Unit>>()
    val favouriteDelete = _favouriteDelete


    val isFavourite = MutableLiveData<Boolean>()


    fun addFavourite(uuid: String, coinDetaill: CoinDetail){

        viewModelScope.launch {
            val favList = FavouriteCoin(
                coinDetaill.id,
                coinDetaill.name,
                coinDetaill.image.small,
                coinDetaill.symbol
            )

            favouriteAdd.value=Resource.loading
            val result = cloudRepo.addFavourite(uuid,favList)
            favouriteAdd.value=result
        }
    }

    fun delete(uuid: String,coinId:String){
        viewModelScope.launch {

            val result= cloudRepo.delete(uuid,coinId)
            favouriteDelete.value=result
        }
    }

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