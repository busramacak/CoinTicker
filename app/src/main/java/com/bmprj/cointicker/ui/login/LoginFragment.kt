package com.bmprj.cointicker.ui.login

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bmprj.cointicker.BuildConfig
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentLoginBinding
import com.bmprj.cointicker.utils.navigate
import com.bmprj.cointicker.utils.toast
import com.bmprj.cointicker.utils.toastLong
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel>()
    private val findNavController by lazy { findNavController() }

    override fun initView(view: View) {
        with(binding) {
            loginButton.setOnClickListener {
                login(eMailEdt.text.toString(), passwEdt.text.toString())
            }
            signupButton.setOnClickListener { signUp() }
            coinGeckoButton.setOnClickListener { openCoinGecko() }
        }
        initTextType()
        if (viewModel.user != null) reload()
        initLiveDataObservers()
    }

    private fun signUp() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        navigate(findNavController,action)
    }

    private fun login(email: String, password: String) {
        viewModel.login(email, password)
    }

    private fun openCoinGecko() {
        val uri = Uri.parse(BuildConfig.COINGECKO_URL)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun reload() {
        if (viewModel.user?.displayName != null) {
            toast(getString(R.string.welcome, viewModel.user?.displayName))
        }
        val action = LoginFragmentDirections.actionLoginFragmentToCoinListFragment()
        navigate(findNavController,action)
    }

    private fun initLiveDataObservers() {

        viewModel.login.handleState(onLoading = {
            binding.progresBar.visibility = View.VISIBLE
        }, onSucces = {
            binding.progresBar.visibility = View.GONE
            reload()
        }, onError = {
            binding.progresBar.visibility = View.GONE
            if (it.message.toString() != "gg") {
                when (it) {
                    is FirebaseAuthInvalidUserException -> {
                        toastLong(R.string.failmsg1)
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        toastLong(R.string.failmsg2)
                    }
                    is FirebaseNetworkException -> {
                        toastLong(R.string.failmsg3)
                    }
                    else -> { toastLong(it.message) }
                }
            }
        })
    }

    private fun initTextType() {
        with(binding) {
            if (getString(R.string.locale) == "tr") {
                materialTextView4.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                materialTextView4.setTextColor(resources.getColor(R.color.textColor1))

                materialTextView5.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                materialTextView5.setTextColor(resources.getColor(R.color.textColor2))
            } else {
                materialTextView4.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                materialTextView4.setTextColor(resources.getColor(R.color.textColor2))

                materialTextView5.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                materialTextView5.setTextColor(resources.getColor(R.color.textColor1))
            }
        }
    }
}


