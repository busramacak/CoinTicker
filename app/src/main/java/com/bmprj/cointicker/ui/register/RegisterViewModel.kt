package com.bmprj.cointicker.ui.register

import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.base.BaseViewModel
import com.bmprj.cointicker.domain.auth.GetAuthUseCase
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: GetAuthUseCase,
) : BaseViewModel() {

    private val _signup = MutableStateFlow<UiState<FirebaseUser>>(UiState.Error(Throwable("gg")))
    val signup = _signup.asStateFlow()

    fun signup(name: String, email: String, password: String) = viewModelScope.launch {
        authUseCase.signUp(name, email, password).customEmit(_signup)
    }
}