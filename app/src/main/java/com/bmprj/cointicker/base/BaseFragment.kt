package com.bmprj.cointicker.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.lifecycleScope
import com.bmprj.cointicker.utils.UiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

abstract class BaseFragment<DB:ViewDataBinding>(private val layout:Int) :Fragment() {


    protected lateinit var binding:DB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,layout,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    abstract fun initView(view:View):Unit

    fun <T> StateFlow<UiState<T>>.handleState(
        onLoading: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onSucces: ((T) -> Unit)? = null
    ) {
        lifecycleScope.launch {
            this@handleState
                .onStart {
                    onLoading?.invoke()
                }
                .catch {
                    onError?.invoke(it)
                }.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            onLoading?.invoke()
                        }

                        is UiState.Success -> {
                            onSucces?.invoke(state.result)
                        }

                        is UiState.Error -> {
                            onError?.invoke(state.error)
                        }
                    }
                }
        }
    }

}