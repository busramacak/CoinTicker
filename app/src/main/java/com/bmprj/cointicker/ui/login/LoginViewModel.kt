package com.bmprj.cointicker.ui.login

import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.base.BaseViewModel
import com.bmprj.cointicker.domain.auth.GetAuthUseCase
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.annotation.Nullable
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: GetAuthUseCase,
    @Nullable private val firebaseUser: FirebaseUser?,
) : BaseViewModel() {

    private val _login = MutableStateFlow<UiState<FirebaseUser>>(UiState.Error(Throwable("gg")))
    val login = _login.asStateFlow()

    val user: FirebaseUser? get() = firebaseUser
    fun login(email: String, password: String) = viewModelScope.launch {
        authUseCase.login(email, password).customEmit(_login)
    }
}