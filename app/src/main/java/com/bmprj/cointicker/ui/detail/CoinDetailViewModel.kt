package com.bmprj.cointicker.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepository
import com.bmprj.cointicker.domain.coins.CoinDetailEntity
import com.bmprj.cointicker.domain.coins.GetCoinUseCase
import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.model.FavouriteCoin
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.annotation.Nullable
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val coinUseCase: GetCoinUseCase,
    private val cloudRepository: CloudRepository,
    @Nullable private val firebaseUser:FirebaseUser?
) :ViewModel(){


    private val _coinDetail = MutableStateFlow<UiState<CoinDetailEntity>>(UiState.Loading)
    val coinDetail = _coinDetail.asStateFlow()

    private val _favouriteAdd = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val favouriteAdd = _favouriteAdd.asStateFlow()

    private val _favouriteDelete = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val favouriteDelete = _favouriteDelete

    private val _isFavourite = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val isFavourite = _isFavourite.asStateFlow()

    val currentUserId = firebaseUser?.uid


    fun setFavouriteState(is_favourite:Boolean) = viewModelScope.launch {
        _isFavourite.emit(UiState.Success(is_favourite))
    }


    fun addFavourite(coinDetaill: CoinDetail) = viewModelScope.launch {
        if (currentUserId == null) return@launch
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
        if (currentUserId == null) return@launch
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
        if (currentUserId == null) return@launch
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