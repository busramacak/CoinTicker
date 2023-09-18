package com.bmprj.cointicker.ui.delete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepositoryImpl
import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepositoryImpl
import com.bmprj.cointicker.domain.auth.GetAuthUseCase
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


    fun deleteAccount() = viewModelScope.launch{
        authUseCase.delete()
            .onStart {
                _deleteAccount.emit(UiState.Loading)
            }.catch {
                _deleteAccount.emit(UiState.Error(it))
            }.collect{
                _deleteAccount.emit(UiState.Success(true))
            }
    }

    fun deleteCloudData() = viewModelScope.launch{
        if(user?.uid==null)return@launch
        cloudRepository.deleteUserInfo(user.uid)
            .onStart {
                _deleteCloud.emit(UiState.Loading)
            }.catch {
                _deleteCloud.emit(UiState.Error(it))
                println(it.message)
            }.collect{
                _deleteCloud.emit(UiState.Success(it))
            }
    }

    fun deleteStorageData() = viewModelScope.launch{
        storageRepository.deletePhoto(user?.uid)
            .onStart {
                _deleteStorage.emit(UiState.Loading)
            }.catch {
                _deleteStorage.emit(UiState.Error(it))
            }.collect{
                _deleteStorage.emit(UiState.Success(true))
            }
    }
}