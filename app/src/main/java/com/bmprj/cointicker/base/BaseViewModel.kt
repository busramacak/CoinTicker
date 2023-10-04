package com.bmprj.cointicker.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmprj.cointicker.utils.UiState
import com.bmprj.cointicker.utils.logError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

abstract class BaseViewModel() : ViewModel() {
    @JvmName("T")
    fun <T> Flow<T>.customEmit(
        state: MutableStateFlow<UiState<T>>,
    ) {
        viewModelScope.launch {
            this@customEmit
                .onStart {
                    state.emit(UiState.Loading)
                }
                .catch {
                    logError(it.message)
                    state.emit(UiState.Error(it))
                }.collect {
                    state.emit(UiState.Success(it))
                }
        }
    }

    @JvmName("UiStateT")
    fun <T> Flow<UiState<T>>.customEmit(
        state: MutableStateFlow<UiState<T>>,
    ) {
        viewModelScope.launch {
            this@customEmit
                .onStart { state.emit(UiState.Loading) }
                .catch {
                    logError(it.message)
                    state.emit(UiState.Error(it))
                }.collect {
                    state.emit(it)
                }
        }
    }

}