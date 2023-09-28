package com.bmprj.cointicker.ui.settings

import android.app.Application
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.base.BaseViewModel
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
class SettingsViewModel @Inject constructor(
    application: Application,
    private val authUseCase: GetAuthUseCase,
    private val storageRepository: StorageRepositoryImpl,
    @Nullable val firebaseUser: FirebaseUser?
): BaseViewModel(application) {

    private val _query  = MutableStateFlow<UiState<Uri>>(UiState.Loading)
    val query = _query.asStateFlow()

    private val _isSuccess = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val isSuccess=_isSuccess.asStateFlow()

    fun getPhoto() = launch{
        if(firebaseUser == null) return@launch
        storageRepository.getPhoto(firebaseUser.uid).customEmit(_query)
    }

    fun changePhoto(uri: Uri?) = launch{
        if(firebaseUser==null) return@launch
        if(uri==null)return@launch

        authUseCase.changePhoto(uri).customEmit(MutableStateFlow(UiState.Loading))
        storageRepository.changePhoto(firebaseUser.uid,uri).customEmit(_isSuccess)
    }

    fun changeUserName(name : String) = launch {
        authUseCase.changeProfileName(name).customEmit(MutableStateFlow(UiState.Loading))
    }

    fun changePassword(next: String) = launch {
        authUseCase.changePassword(next).customEmit(MutableStateFlow(UiState.Loading))
    }
}
