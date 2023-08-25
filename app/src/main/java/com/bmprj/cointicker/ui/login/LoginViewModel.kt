package com.bmprj.cointicker.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.domain.GetLoginUseCase
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: GetLoginUseCase
):ViewModel() {

    private val _login = MutableLiveData<UiState<FirebaseUser>?>()
    val login : LiveData<UiState<FirebaseUser>?> = _login

    val currentUser: FirebaseUser?
        get() = loginUseCase.currentUser


    fun login(email:String, password:String) = viewModelScope.launch {

            loginUseCase.login(email,password)
                .onStart {
                    _login.value=UiState.Loading
                }
                .catch {
                    _login.value=UiState.Error(it)
                }
                .collect{
                    _login.value=it
                }
    }
}