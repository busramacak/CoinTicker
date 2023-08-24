package com.bmprj.cointicker.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.coin.CoinRepositoryImpl
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepository
import com.bmprj.cointicker.domain.coins.CoinDetailEntity
import com.bmprj.cointicker.domain.coins.GetCoinUseCase
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.model.FavouriteCoin
import com.bmprj.cointicker.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val coinUseCase: GetCoinUseCase,
    private val cloudRepository: CloudRepository
) :ViewModel(){

    val coinDetail = MutableLiveData<UiState<CoinDetailEntity>>()

    private val _favouriteAdd = MutableLiveData<Resource<Boolean>>()
    val favouriteAdd = _favouriteAdd

    private val _favouriteDelete = MutableLiveData<Resource<Boolean>>()
    val favouriteDelete = _favouriteDelete

    val isFavourite = MutableLiveData<Resource<Boolean>>()

    val currentUserId = authRepository.currentUser?.uid!!


    fun addFavourite(coinDetaill: CoinDetail) = viewModelScope.launch {

        val favList = FavouriteCoin(
            coinDetaill.id,
            coinDetaill.name,
            coinDetaill.image.small,
            coinDetaill.symbol
        )


        cloudRepository.addFavourite(currentUserId,favList)
            .onStart {
                favouriteAdd.value= Resource.loading
            }
            .catch {
                favouriteAdd.value=Resource.Failure(it)
            }
            .collect{
                favouriteAdd.value=Resource.Success(it)
            }

    }

    fun getFavourite(coinId: String) = viewModelScope.launch {

        cloudRepository.getFavourite(currentUserId,coinId)
            .onStart {
                isFavourite.value=Resource.loading
            }
            .catch {
                isFavourite.value=Resource.Failure(it)
            }
            .collect{
                isFavourite.value=Resource.Success(it)
            }

    }

    fun delete(coinId:String) = viewModelScope.launch {

        cloudRepository.delete(currentUserId,coinId)
            .onStart {
                favouriteDelete.value=Resource.loading
            }
            .catch {
                favouriteDelete.value=Resource.Failure(it)
            }
            .collect{
                favouriteDelete.value=Resource.Success(it)
            }

    }

    fun getCoin(id:String) = viewModelScope.launch {

        coinUseCase.getCoin(id)
            .onStart {
                coinDetail.value=UiState.Loading
            }
            .catch {
                coinDetail.value=UiState.Error(it)
            }
            .collect{
                coinDetail.value=it
            }
    }
}