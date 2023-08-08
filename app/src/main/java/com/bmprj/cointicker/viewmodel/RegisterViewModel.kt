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
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {


    private val _signup = MutableLiveData<Resource<FirebaseUser>?>()
    val signup: LiveData<Resource<FirebaseUser>?> = _signup


    fun signup(view: View, name: String, email: String, password: String) = viewModelScope.launch {
        _signup.value = Resource.loading
        val result = repository.signup(name, email, password)
        _signup.value = result
        Toast.makeText(view.context, "signUPppp", Toast.LENGTH_LONG).show()
    }
}