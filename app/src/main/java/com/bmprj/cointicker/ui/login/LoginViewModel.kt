package com.bmprj.cointicker.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.utils.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
):ViewModel() {

    private val _login = MutableLiveData<Resource<FirebaseUser>?>()
    val login : LiveData<Resource<FirebaseUser>?> = _login

    val currentUser: FirebaseUser?
        get() = authRepository.currentUser


    fun login(email:String, password:String) = viewModelScope.launch {

            authRepository.login(email,password)
                .onStart {
                    _login.value=Resource.loading
                }
                .catch {
                    _login.value=Resource.Failure(it)
                }
                .collect{
                    _login.value=Resource.Success(it)
                }
    }
}