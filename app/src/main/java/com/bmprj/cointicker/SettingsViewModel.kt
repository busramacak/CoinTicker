package com.bmprj.cointicker

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.data.remote.firebase.StorageRepository
import com.bmprj.cointicker.data.remote.firebase.StorageRepositoryImpl
import com.bmprj.cointicker.data.remote.firebase.cloud.CloudRepository
import com.bmprj.cointicker.domain.auth.GetAuthUseCase
import com.bmprj.cointicker.model.FavouriteCoin
import com.bmprj.cointicker.utils.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authUseCase: GetAuthUseCase,
    private val storageRepository: StorageRepositoryImpl
): ViewModel() {


    val query  = MutableLiveData<Resource<Uri>>()

    val isSuccess = MutableLiveData<Resource<Boolean>>()
    val currentUser :FirebaseUser ?
        get() = authUseCase.currentUser


    fun getPhoto() = viewModelScope.launch{
        storageRepository.getPhoto(currentUser?.uid!!)
            .onStart {
                query.value= Resource.loading
            }
            .catch {
                query.value=Resource.Failure(it)
            }
            .collect{
                query.value=Resource.Success(it)
            }
    }

    fun changePhoto(uri: Uri?) = viewModelScope.launch{
        authUseCase.changePhoto(uri)
            .onStart {
                Resource.loading
            }
            .catch {
                Resource.Failure(it)
            }
            .collect{
                Resource.Success(it)
            }

        storageRepository.changePhoto(currentUser?.uid!!,uri!!)
            .onStart {
                isSuccess.value=Resource.loading
            }
            .catch {
                isSuccess.value= Resource.Failure(it)
            }
            .collect{
                isSuccess.value= Resource.Success(it)
            }
    }

    fun changeUserName(name : String) = viewModelScope.launch {
        authUseCase.changeProfileName(name)
            .onStart {
                Resource.loading
            }
            .catch {
                Resource.Failure(it)
            }
            .collect{
                Resource.Success(it)
            }
    }

    fun changePassword( next: String) = viewModelScope.launch {

        authUseCase.changePassword(next)
            .onStart {
                Resource.loading
            }
            .catch {
                Resource.Failure(it)
            }
            .collect{
                Resource.Success(it)
            }
    }

}
