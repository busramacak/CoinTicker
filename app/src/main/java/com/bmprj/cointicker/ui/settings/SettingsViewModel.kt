package com.bmprj.cointicker.ui.settings

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.base.BaseViewModel
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
class SettingsViewModel @Inject constructor(
    private val authUseCase: GetAuthUseCase,
    private val storageRepository: StorageRepositoryImpl,
    @Nullable val firebaseUser: FirebaseUser?,
) : BaseViewModel() {

    private val _query = MutableStateFlow<UiState<Uri>>(UiState.Loading)
    val query = _query.asStateFlow()
    private val _isSuccess = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val isSuccess = _isSuccess.asStateFlow()
    private val _reEntry = MutableStateFlow<UiState<Void?>>(UiState.Loading)
    val reEntry = _reEntry.asStateFlow()

    private val _isPasswordChange = MutableStateFlow<UiState<Void?>>(UiState.Loading)
    val isPasswordChange = _isPasswordChange.asStateFlow()

    private val _logOut = MutableStateFlow<UiState<FirebaseAuthResources<Unit>>>(UiState.Loading)
    val logOut = _logOut.asStateFlow()


    fun logOut() = viewModelScope.launch {
        if (firebaseUser == null) return@launch
        authUseCase.logout().customEmit(_logOut)
    }

    fun getPhoto() = viewModelScope.launch {
        if (firebaseUser == null) return@launch
        storageRepository.getPhoto(firebaseUser.uid).customEmit(_query)
    }

    fun reEntryUser(email: String, password: String) = viewModelScope.launch {
        if (firebaseUser == null) return@launch
        authUseCase.reEntryUser(email, password).customEmit(_reEntry)
    }

    fun changePhoto(uri: Uri?) = viewModelScope.launch {
        if (firebaseUser == null) return@launch
        if (uri == null) return@launch

        authUseCase.changePhoto(uri).customEmit(MutableStateFlow(UiState.Loading))
        storageRepository.changePhoto(firebaseUser.uid, uri).customEmit(_isSuccess)
    }

    fun changeUserName(name: String) = viewModelScope.launch {
        authUseCase.changeProfileName(name).customEmit(MutableStateFlow(UiState.Loading))
    }

    fun changePassword(next: String) = viewModelScope.launch {
        authUseCase.changePassword(next).customEmit(_isPasswordChange)
    }
}
