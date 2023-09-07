package com.bmprj.cointicker.ui.register

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
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
        viewModel.signup.observe(viewLifecycleOwner){resource->
            when(resource){
                is UiState.Success->{
                    binding.progress.visibility=View.GONE
                    Toast.makeText(view.context,getString(R.string.succesmsg1),Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is UiState.Error-> {
                    binding.progress.visibility=View.GONE
                    when (resource.error) {
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
                is UiState.Loading->{
                    binding.progress.visibility=View.VISIBLE

                }

                else -> {}
            }
        }
    }
}