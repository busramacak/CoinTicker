package com.bmprj.cointicker.ui.settings

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.firebase.storage.StorageRepositoryImpl
import com.bmprj.cointicker.domain.auth.GetAuthUseCase
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authUseCase: GetAuthUseCase,
    private val storageRepository: StorageRepositoryImpl
): ViewModel() {


    private val _query  = MutableStateFlow<UiState<Uri>>(UiState.Loading)
    val query = _query.asStateFlow()

    private val _isSuccess = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val isSuccess=_isSuccess.asStateFlow()
    val currentUser :FirebaseUser ?
        get() = authUseCase.currentUser


    fun getPhoto() = viewModelScope.launch{
        storageRepository.getPhoto(currentUser?.uid!!)
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

        storageRepository.changePhoto(currentUser?.uid!!,uri!!)
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

    fun changePassword( next: String) = viewModelScope.launch {

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
