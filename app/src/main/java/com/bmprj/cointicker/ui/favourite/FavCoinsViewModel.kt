package com.bmprj.cointicker.ui.favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepository
import com.bmprj.cointicker.utils.Resource
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
class FavCoinsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val cloudRepository : CloudRepository
):ViewModel() {

    private val _favCoins  = MutableStateFlow<UiState<List<FavouriteCoin>>>(UiState.Loading)
    val favCoins = _favCoins.asStateFlow()

    val currentUser = authRepository.currentUser?.uid!!


    fun getFavCoins() = viewModelScope.launch {
        cloudRepository.getAllFavourites(currentUser)
            .onStart {
                _favCoins.emit(UiState.Loading)
            }
            .catch {
                _favCoins.emit(UiState.Error(it))
            }
            .collect{
                _favCoins.emit(UiState.Success(it))
            }
    }
}