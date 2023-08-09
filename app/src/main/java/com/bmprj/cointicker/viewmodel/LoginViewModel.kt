package com.bmprj.cointicker.viewmodel

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.auth.AuthRepository
import com.bmprj.cointicker.data.auth.Resource
import com.bmprj.cointicker.data.auth.Resource.Failure
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
):ViewModel() {

    private val _login = MutableLiveData<Resource<FirebaseUser>?>()
    val login : LiveData<Resource<FirebaseUser>?> = _login

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    fun login(email:String, password:String) = viewModelScope.launch {

            _login.value= Resource.loading
            val result =  repository.login(email,password)
            _login.value=result

    }
}