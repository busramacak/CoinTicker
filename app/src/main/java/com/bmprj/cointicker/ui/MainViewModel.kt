package com.bmprj.cointicker.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.db.CoinDAO
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val coinDAO: CoinDAO
):ViewModel(){
    val filteredCoins = MutableLiveData<ArrayList<Entity>>() // Filtrelenmiş sonuçlar


    fun logOut(){
        authRepository.logout()
    }

    fun getDataFromDatabase(query:String) =  viewModelScope.launch {

        val aList = ArrayList<Entity>(coinDAO.getCoin(query))
        filteredCoins.value=aList
    }

}