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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _coinDetail = MutableStateFlow<UiState<CoinDetailEntity>>(UiState.Loading)
    val coinDetail = _coinDetail.asStateFlow()

    private val _favouriteAdd = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val favouriteAdd = _favouriteAdd.asStateFlow()

    private val _favouriteDelete = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val favouriteDelete = _favouriteDelete

    val _isFavourite = MutableLiveData<UiState<Boolean>>()


    val currentUserId = authRepository.currentUser?.uid!!


    fun addFavourite(coinDetaill: CoinDetail) = viewModelScope.launch {

        val favList = FavouriteCoin(coinDetaill.id, coinDetaill.name, coinDetaill.image.small, coinDetaill.symbol)

        cloudRepository.addFavourite(currentUserId,favList)
            .onStart {
                _favouriteAdd.emit(UiState.Loading)
            }
            .catch {
                _favouriteAdd.emit(UiState.Error(it))
            }
            .collect{
                _favouriteAdd.emit(UiState.Success(it))
            }

    }

    fun getFavourite(coinId: String) = viewModelScope.launch {

        cloudRepository.getFavourite(currentUserId,coinId)
            .onStart {
                _isFavourite.value = UiState.Loading
            }
            .catch {
                _isFavourite.value=UiState.Error(it)
            }
            .collect{
                _isFavourite.value=UiState.Success(it)
            }
    }

    fun delete(coinId:String) = viewModelScope.launch {

        cloudRepository.delete(currentUserId,coinId)
            .onStart {
                _favouriteDelete.emit(UiState.Loading)
            }
            .catch {
                _favouriteDelete.emit(UiState.Error(it))
            }
            .collect{
                _favouriteDelete.emit(UiState.Success(it))
            }
    }

    fun getCoin(id:String) = viewModelScope.launch {

        coinUseCase.getCoin(id)
            .onStart {
                _coinDetail.emit(UiState.Loading)
            }
            .catch {
                _coinDetail.emit(UiState.Error(it))
            }
            .collect{
                _coinDetail.emit(it)
            }
    }
}