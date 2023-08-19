package com.bmprj.cointicker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.coin.CoinUtils
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepository
import com.bmprj.cointicker.data.remote.firebase.di.Resource
import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.model.FavouriteCoin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val apiUtil: CoinUtils,
    private val cloudRepo: CloudRepository
) :ViewModel(){

    val coinDetail = MutableLiveData<CoinDetail?>()

    private val _favouriteAdd = MutableLiveData<Resource<Unit>>()
    val favouriteAdd = _favouriteAdd

    private val _favouriteDelete = MutableLiveData<Resource<Unit>>()
    val favouriteDelete = _favouriteDelete

    val isFavourite = MutableLiveData<Resource<Boolean>>()

    val currentUserId = repository.currentUser?.uid!!


    fun addFavourite(coinDetaill: CoinDetail){

        viewModelScope.launch {
            val favList = FavouriteCoin(
                coinDetaill.id,
                coinDetaill.name,
                coinDetaill.image.small,
                coinDetaill.symbol
            )

            favouriteAdd.value= Resource.loading
            val result = cloudRepo.addFavourite(currentUserId,favList)
            favouriteAdd.value=result
        }
    }

    fun getFavourite(coinId: String){
        viewModelScope.launch {
            val result = cloudRepo.getFavourite(currentUserId,coinId)
            isFavourite.value=result
        }
    }

    fun delete(coinId:String){
        viewModelScope.launch {

            val result= cloudRepo.delete(currentUserId,coinId)
            favouriteDelete.value=result
        }
    }

    fun getCoin(id:String){
        viewModelScope.launch {

            val r = apiUtil.getCoin(id).body()
            coinDetail.value=r



//            CoinDetail(id=bitcoin, name=Bitcoin, image=İmage(large=https://assets.coingecko.com/coins/images/1/large/bitcoin.png?1547033579, small=https://assets.coingecko.com/coins/images/1/small/bitcoin.png?1547033579, thumb=https://assets.coingecko.com/coins/images/1/thumb/bitcoin.png?1547033579), lastUpdated=null, marketData=null, description=null, symbol=btc)
//                2023-08-11 16:07:27.431 19254-19254

//            println(r)
//            val list = ArrayList<CoinDetail>()
//            val c = CoinDetail(r?.id!!,r.name,r.image,r.lastUpdated,r.marketData,r.description,r.symbol)
//            list.add(c)

        }

    }
}