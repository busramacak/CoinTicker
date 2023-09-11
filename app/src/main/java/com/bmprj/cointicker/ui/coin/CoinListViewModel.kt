package com.bmprj.cointicker.ui.coin

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.coin.CoinRepositoryImpl
import com.bmprj.cointicker.data.db.CoinDAO
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepository
import com.bmprj.cointicker.domain.auth.GetAuthUseCase
import com.bmprj.cointicker.domain.coin.CoinEntity
import com.bmprj.cointicker.domain.coin.GetCoinsUseCase
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.utils.FirebaseAuthResources
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(
    private val coinsUseCase: GetCoinsUseCase,
    private val coinDAO: CoinDAO,
    private val authUseCase: GetAuthUseCase,
    private val storageRepository: StorageRepository
) :ViewModel() {

    private val _coins = MutableStateFlow<UiState<CoinEntity>>(UiState.Loading)
    val coins = _coins.asStateFlow()
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
                _userInfo.emit(UiState.Error(Throwable("gg")))
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
    fun getData() =  viewModelScope.launch{
        coinsUseCase.getCoins()
            .onStart {
                _coins.emit(UiState.Loading)
            }
            .catch {
                _coins.emit(UiState.Error(it))
            }
            .collect{
                _coins.emit(it)
            }
    }

    fun insertCoins(list : ArrayList<CoinMarketItem>) =  viewModelScope.launch {
        coinDAO.insertAllCoins(marketItemToEntity(list))
    }

    private fun marketItemToEntity(marketItemList:ArrayList<CoinMarketItem>):List<Entity>{
        return marketItemList.map {
            Entity(it.currentPrice, it.id, it.image, it.name, it.symbol)
        }
    }
}