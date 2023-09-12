package com.bmprj.cointicker.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.bmprj.cointicker.R
import com.bmprj.cointicker.utils.UiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

abstract class BaseActivity<VDB:ViewDataBinding>():AppCompatActivity() {

    protected abstract val binding:VDB

    abstract fun initView():Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_CoinTicker)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }
}