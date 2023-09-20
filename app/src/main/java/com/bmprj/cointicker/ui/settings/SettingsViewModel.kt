package com.bmprj.cointicker.ui.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val authUseCase: GetAuthUseCase,
    private val storageRepository: StorageRepositoryImpl,
    @Nullable val firebaseUser: FirebaseUser?
): ViewModel() {


    private val _query  = MutableStateFlow<UiState<Uri>>(UiState.Loading)
    val query = _query.asStateFlow()

    private val _isSuccess = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val isSuccess=_isSuccess.asStateFlow()


    fun getPhoto() = viewModelScope.launch{
        if(firebaseUser == null) return@launch
        storageRepository.getPhoto(firebaseUser.uid)
            .onStart {
                _query.emit(UiState.Loading)
            }
            .catch {
                _query.emit(UiState.Error(it))
            }
            .collect{
                _query.emit(UiState.Success(it))
            }
    }

    fun changePhoto(uri: Uri?) = viewModelScope.launch{
        if(firebaseUser==null) return@launch
        authUseCase.changePhoto(uri)
            .onStart {
                UiState.Loading
            }
            .catch {
                UiState.Error(it)
            }
            .collect{
                UiState.Success(it)
            }

        if(uri==null)return@launch

        storageRepository.changePhoto(firebaseUser.uid,uri)
            .onStart {
                _isSuccess.emit(UiState.Loading)
            }
            .catch {
                _isSuccess.emit(UiState.Error(it))
            }
            .collect{
                _isSuccess.emit(UiState.Success(it))
            }
    }

    fun changeUserName(name : String) = viewModelScope.launch {
        authUseCase.changeProfileName(name)
            .onStart {
                UiState.Loading
            }
            .catch {
                UiState.Error(it)
            }
            .collect{
                UiState.Success(it)
            }
    }

    fun changePassword(next: String) = viewModelScope.launch {
        authUseCase.changePassword(next)
            .onStart {
                UiState.Loading
            }
            .catch {
                UiState.Error(it)
            }
            .collect{
                UiState.Success(it)
            }
    }
}
