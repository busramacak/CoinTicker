package com.bmprj.cointicker.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.firebase.auth.AuthRepository
import com.bmprj.cointicker.utils.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signup = MutableLiveData<Resource<FirebaseUser>?>()
    val signup: LiveData<Resource<FirebaseUser>?> = _signup


    fun signup(name: String, email: String, password: String) = viewModelScope.launch {
        _signup.value = Resource.loading
        authRepository.signup(name, email, password)
            .onStart {
                _signup.value=Resource.loading
            }
            .catch{
                _signup.value=Resource.Failure(it)
            }
            .collect{
                _signup.value=Resource.Success(it)
            }
    }
}