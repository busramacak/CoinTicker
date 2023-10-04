package com.bmprj.cointicker.ui.delete

import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.base.BaseViewModel
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepositoryImpl
import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepositoryImpl
import com.bmprj.cointicker.domain.auth.GetAuthUseCase
import com.bmprj.cointicker.utils.FirebaseAuthResources
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.annotation.Nullable
import javax.inject.Inject

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val authUseCase: GetAuthUseCase,
    private val cloudRepository: CloudRepositoryImpl,
    private val storageRepository: StorageRepositoryImpl,
    @Nullable private val user: FirebaseUser?,
) : BaseViewModel() {

    private val _deleteAccount = MutableStateFlow<UiState<Void?>>(UiState.Loading)
    val deleteAccount = _deleteAccount.asStateFlow()

    private val _deleteCloud = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val deleteCloud = _deleteCloud.asStateFlow()

    private val _deleteStorage = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val deleteStorage = _deleteStorage.asStateFlow()

    private val _reEntry = MutableStateFlow<UiState<Void?>>(UiState.Loading)
    val reEntry = _reEntry.asStateFlow()

    private val _logOut = MutableStateFlow<UiState<FirebaseAuthResources<Unit>>>(UiState.Loading)
    val logOut = _logOut.asStateFlow()

    val currentUser = user

    fun logOut() = viewModelScope.launch {
        if (currentUser == null) return@launch
        authUseCase.logout().customEmit(_logOut)
    }

    fun reEntryUser(email: String, password: String) = viewModelScope.launch {
        if (currentUser == null) return@launch
        authUseCase.reEntryUser(email, password).customEmit(_reEntry)
    }

    fun deleteAccount() = viewModelScope.launch {
        if (currentUser == null) return@launch
        authUseCase.delete().customEmit(_deleteAccount)
    }

    fun deleteCloudData() = viewModelScope.launch {
        if (currentUser?.uid == null) return@launch
        cloudRepository.deleteUserInfo(currentUser.uid).customEmit(_deleteCloud)
    }

    fun deleteStorageData() = viewModelScope.launch {
        if (currentUser?.uid == null) return@launch
        storageRepository.deletePhoto(currentUser.uid).customEmit(_deleteStorage)
    }
}