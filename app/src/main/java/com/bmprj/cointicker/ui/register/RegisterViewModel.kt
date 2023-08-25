package com.bmprj.cointicker.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.domain.GetAuthUseCase
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: GetAuthUseCase
) : ViewModel() {

    private val _signup = MutableLiveData<UiState<FirebaseUser>>()
    val signup: LiveData<UiState<FirebaseUser>> = _signup


    fun signup(name: String, email: String, password: String) = viewModelScope.launch {
        authUseCase.signUp(name, email, password)
            .onStart {
                _signup.value=UiState.Loading
            }
            .catch{
                _signup.value=UiState.Error(it)
            }
            .collect{
                _signup.value=it
            }
    }
}