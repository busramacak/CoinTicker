package com.bmprj.cointicker.ui

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.db.CoinDAO
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepositoryImpl
import com.bmprj.cointicker.domain.auth.GetAuthUseCase
import com.bmprj.cointicker.utils.FirebaseAuthResources
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val _filteredCoins = MutableStateFlow<ArrayList<Entity>>(arrayListOf()) // Filtrelenmiş sonuçlar
    val filteredCoins = _filteredCoins.asStateFlow()

    private val _userInfo = MutableStateFlow<UiState<Uri>>(UiState.Loading)
    val userInfo = _userInfo.asStateFlow()

    private val _logOut = MutableStateFlow<UiState<FirebaseAuthResources<Unit>>>(UiState.Loading)
    val logOut = _logOut.asStateFlow()

    val currentUser: FirebaseUser?
        get() = authUseCase.currentUser

    fun logOut() = viewModelScope.launch{
        authUseCase.logout()
            .onStart {
                _logOut.emit(UiState.Loading)
            }
            .catch {
                _logOut.emit(UiState.Error(it))
            }
            .collect{
                _logOut.emit(it)
            }
    }

    fun getUserInfo() = viewModelScope.launch{

        if(authUseCase.currentUser!=null){
            storageRepository.getPhoto(currentUser?.uid!!)
                .onStart {
                    _userInfo.emit(UiState.Loading)
                }
                .catch {
                    _userInfo.emit(UiState.Error(it))
                }
                .collect{
                    _userInfo.emit(UiState.Success(it))
                }
        }

    }

    fun getDataFromDatabase(query:String) =  viewModelScope.launch {
        val aList = ArrayList<Entity>(coinDAO.getCoin(query))
        _filteredCoins.emit(aList)
    }

}