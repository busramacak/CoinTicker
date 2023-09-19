package com.bmprj.cointicker.ui.delete

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepositoryImpl
import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepositoryImpl
import com.bmprj.cointicker.domain.auth.GetAuthUseCase
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.annotation.Nullable
import javax.inject.Inject

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val authUseCase: GetAuthUseCase,
    private val cloudRepository: CloudRepositoryImpl,
    private val storageRepository: StorageRepositoryImpl,
    @Nullable private val user: FirebaseUser?
):ViewModel() {

    private val _deleteAccount = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val deleteAccount = _deleteAccount.asStateFlow()

    private val _deleteCloud = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val deleteCloud = _deleteCloud.asStateFlow()

    private val _deleteStorage = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val deleteStorage = _deleteStorage.asStateFlow()

//    private val _reEntry = MutableStateFlow<UiState<FirebaseUser>>(UiState.Loading)
//    val reEntry = _reEntry.asStateFlow()

    val currentUser = user


    fun reEntryUser(email:String,password:String) = viewModelScope.launch{
        if(currentUser==null)return@launch

        currentUser.reauthenticate(EmailAuthProvider.getCredential(email, password)).addOnCompleteListener {
            deleteCloudData()
        }
//        authUseCase.login(email, password)
//            .catch {
//                _reEntry.emit(UiState.Error(it))
//            }.collect{
//                _reEntry.emit(it)
//            }
    }
    fun deleteAccount() = viewModelScope.launch{
        if(currentUser==null)return@launch
        authUseCase.delete()
            .onStart {
                _deleteAccount.emit(UiState.Loading)
            }.catch {
                Log.e("deleteAccountCatch",it.message.toString())
                _deleteAccount.emit(UiState.Error(it))
            }.collect{
                Log.e("deleteAccountCollect",true.toString())
                _deleteAccount.emit(UiState.Success(true))
            }
    }

    fun deleteCloudData() = viewModelScope.launch{
        if(currentUser?.uid==null)return@launch
        cloudRepository.deleteUserInfo(currentUser.uid)
            .onStart {
                _deleteCloud.emit(UiState.Loading)
            }.catch {
                _deleteCloud.emit(UiState.Error(it))
                Log.e("deleteCloudDataCatch",it.message.toString())
            }.collect{
                Log.e("deleteCloudDataCollect",it.toString())
                _deleteCloud.emit(UiState.Success(it))
            }
    }

    fun deleteStorageData() = viewModelScope.launch{
        if(currentUser?.uid==null)return@launch
        storageRepository.deletePhoto(currentUser.uid)
            .onStart {
                _deleteStorage.emit(UiState.Loading)
            }.catch {
                Log.e("deleteStorageDataCatch",it.message.toString())
                _deleteStorage.emit(UiState.Error(it))
            }.collect{
                Log.e("deleteStorageDataCollect",true.toString())
                _deleteStorage.emit(UiState.Success(true))
            }
    }
}