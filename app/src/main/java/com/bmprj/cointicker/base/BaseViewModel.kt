package com.bmprj.cointicker.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.utils.UiState
import com.bmprj.cointicker.utils.logError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application) : AndroidViewModel(application),CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job+Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


    @JvmName("T")
    fun <T> Flow<T>.customEmit(
        state: MutableStateFlow<UiState<T>>
    ) {
        viewModelScope.launch {
            this@customEmit
                .onStart {
                    state.emit(UiState.Loading)
                }
                .catch {
                    logError(it.message)
                    state.emit(UiState.Error(it))
                }.collect{
                    state.emit(UiState.Success(it))
                }
        }
    }

    @JvmName("UiStateT")
    fun <T> Flow<UiState<T>>.customEmit(
        state:MutableStateFlow<UiState<T>>
    ){
        viewModelScope.launch {
            this@customEmit
                .onStart { state.emit(UiState.Loading) }
                .catch {
                    logError(it.message)
                    state.emit(UiState.Error(it))
                }.collect{
                    state.emit(it)
                }
        }
    }

}