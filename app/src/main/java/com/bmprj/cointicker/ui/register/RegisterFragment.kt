package com.bmprj.cointicker.ui.register

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentRegisterBinding
import com.bmprj.cointicker.utils.Encryption
import com.bmprj.cointicker.utils.toast
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.AndroidEntryPoint
import java.util.Base64


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

   private val viewModel by viewModels<RegisterViewModel>()
    private val findNavController by lazy { findNavController() }

    override fun initView(view: View) {
        binding.register=this
        initLiveDataObservers()
    }

    fun login(){
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController.navigate(action)
    }

    fun signup(name:String, email:String, password:String){
        viewModel.signup(name, email, password)
    }

    private fun initLiveDataObservers(){

        viewModel.signup.handleState(
            onLoading = {
                binding.progresBar.visibility=View.VISIBLE
            },
            onError = {
                binding.progresBar.visibility=View.GONE
                if(it.message!="gg"){
                    when (it) {
                        is FirebaseAuthWeakPasswordException -> {
                            toast(R.string.failmsg5)
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            toast(R.string.failmsg6)
                        }
                        is FirebaseAuthUserCollisionException -> {
                            toast(R.string.failmsg7)
                        }
                        is FirebaseNetworkException -> {
                            toast(R.string.failmsg8)
                        }
                        else -> { toast(it.message) }
                    }
                }
            },
            onSucces = {
                binding.progresBar.visibility=View.GONE
                toast(R.string.succesmsg1)
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController.navigate(action)
            }
        )
    }
}