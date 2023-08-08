package com.bmprj.cointicker.viewmodel

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.auth.AuthRepository
import com.bmprj.cointicker.data.auth.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init{
        if(repository.currentUser != null){
            _login.value = Resource.Success(repository.currentUser!!)
        }
    }
    fun login(view: View, email:String, password:String) = viewModelScope.launch {
        _login.value= Resource.loading
        val result =  repository.login(email,password)
        _login.value=result

        Toast.makeText(view.context,"logggg", Toast.LENGTH_LONG).show()
    }
}