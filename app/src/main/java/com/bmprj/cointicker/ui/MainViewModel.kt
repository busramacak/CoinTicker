package com.bmprj.cointicker.ui

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.db.CoinDAO
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.data.remote.firebase.StorageRepository
import com.bmprj.cointicker.data.remote.firebase.StorageRepositoryImpl
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.domain.auth.GetAuthUseCase
import com.bmprj.cointicker.utils.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUseCase: GetAuthUseCase,
    private val coinDAO: CoinDAO,
    private val storageRepository: StorageRepositoryImpl
):ViewModel(){
    val filteredCoins = MutableLiveData<ArrayList<Entity>>() // Filtrelenmiş sonuçlar
    val userInfo = MutableLiveData<Resource<Uri>>()
    val currentUser: FirebaseUser?
        get() = authUseCase.currentUser

    fun logOut() = viewModelScope.launch{
        authUseCase.logout()
    }

    fun getUserInfo() = viewModelScope.launch{
        storageRepository.getPhoto(currentUser?.uid!!)
            .onStart {
                userInfo.value= Resource.loading
            }
            .catch {
                userInfo.value=Resource.Failure(it)
            }
            .collect{
                userInfo.value=Resource.Success(it)
            }
    }

    fun getDataFromDatabase(query:String) =  viewModelScope.launch {

        val aList = ArrayList<Entity>(coinDAO.getCoin(query))
        filteredCoins.value=aList
    }

}