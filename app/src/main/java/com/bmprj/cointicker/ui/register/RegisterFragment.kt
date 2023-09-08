package com.bmprj.cointicker.ui.register

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bmprj.cointicker.R
import com.bmprj.cointicker.databinding.FragmentRegisterBinding
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

   private val viewModel by viewModels<RegisterViewModel>()


    override fun initView(view: View) {
        binding.register=this
        initLiveDataObservers(view)
    }

    fun login(view:View){
        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
    }

    fun signup(name:String,email:String, password:String){
        viewModel.signup(name, email, password)
    }

    private fun initLiveDataObservers(view: View){

        lifecycleScope.launch {
            viewModel.signup.handleState(
                onLoading = {
                    binding.progress.visibility=View.VISIBLE
                },
                onError = {
                    binding.progress.visibility=View.GONE
                    if(it.message!="gg"){
                        when (it) {
                            is FirebaseAuthWeakPasswordException -> {
                                // Zayıf bir şifre kullanıldı
                                Toast.makeText(
                                    view.context,
                                    getString(R.string.failmsg5),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                // Geçersiz kimlik bilgileri
                                Toast.makeText(
                                    view.context,
                                    getString(R.string.failmsg6),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                            is FirebaseAuthUserCollisionException -> {
                                // Kullanıcı zaten mevcut
                                Toast.makeText(
                                    view.context,
                                    getString(R.string.failmsg7),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                            is FirebaseNetworkException -> {
                                // İnternet bağlantısı yok veya sunucuya erişilemiyor
                                Toast.makeText(
                                    view.context,
                                    getString(R.string.failmsg8),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                            else -> {
                                // Diğer hatalar
                                Toast.makeText(
                                    view.context,
                                    getString(R.string.failmsg9),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                    }

                },
                onSucces = {
                    binding.progress.visibility=View.GONE
                    Toast.makeText(view.context,getString(R.string.succesmsg1),Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
                }
            )
        }
    }
}