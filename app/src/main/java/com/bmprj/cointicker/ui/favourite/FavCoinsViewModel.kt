package com.bmprj.cointicker.ui.favourite

import android.app.Application
import com.bmprj.cointicker.base.BaseViewModel
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepository
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
class FavCoinsViewModel @Inject constructor(
    application: Application,
    private val cloudRepository : CloudRepository,
    @Nullable private val firebaseUser: FirebaseUser?
):BaseViewModel(application) {

    private val _favCoins  = MutableStateFlow<UiState<List<FavouriteCoin>>>(UiState.Loading)
    val favCoins = _favCoins.asStateFlow()

    val currentUserId = firebaseUser?.uid

    fun getFavCoins() = launch {
        if(currentUserId==null)return@launch
        cloudRepository.getAllFavourites(currentUserId).customEmit(_favCoins)
    }
}