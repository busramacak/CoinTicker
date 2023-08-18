package com.bmprj.cointicker.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepository
import com.bmprj.cointicker.data.remote.firebase.di.Resource
import com.bmprj.cointicker.model.FavouriteCoin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavCoinsViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val cloudRepo : CloudRepository
):ViewModel() {

    val favCoins  = MutableLiveData<Resource<List<FavouriteCoin>>?>()

    val currentUser = repository.currentUser?.uid!!


    fun getFavCoins(){
        favCoins.value=null
        viewModelScope.launch {

            val resource = cloudRepo.getAllFavourites(currentUser)

            favCoins.value=resource
            println(resource)

        }
    }
}