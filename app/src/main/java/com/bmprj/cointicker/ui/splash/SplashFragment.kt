package com.bmprj.cointicker.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bmprj.cointicker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private val scope by lazy { CoroutineScope(Dispatchers.Main) }
    private val findNavController by lazy { findNavController() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {



        scope.launch {
            delay(1000)
            val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            findNavController.navigate(action)
        }
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}