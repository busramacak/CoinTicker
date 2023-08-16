package com.bmprj.cointicker.view.auth

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bmprj.cointicker.R
import com.bmprj.cointicker.data.firebase.di.Resource
import com.bmprj.cointicker.databinding.FragmentRegisterBinding
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.viewmodel.RegisterViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

   private val viewModel by viewModels<RegisterViewModel>()

    override fun setUpViews(view: View) {
        super.setUpViews(view)
        binding.register=this

        observeLiveData(view)
    }

    fun login(view:View){
        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)

    }

    fun signup(name:String,email:String, password:String){
        viewModel.signup(name, email, password)
    }

    private fun observeLiveData(view: View){
        viewModel.signup.observe(viewLifecycleOwner){resource->
            when(resource){
                is Resource.Success ->{
                    Toast.makeText(view.context,"Hesap oluşturma başarılı.",Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is Resource.Failure -> {
                    when (resource.exception) {
                        is FirebaseAuthWeakPasswordException -> {
                            // Zayıf bir şifre kullanıldı
                            Toast.makeText(
                                view.context,
                                "Daha güçlü bir şifre seçin.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        is FirebaseAuthInvalidCredentialsException -> {
                            // Geçersiz kimlik bilgileri
                            Toast.makeText(
                                view.context,
                                "Geçersiz e-posta adresi veya şifre.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        is FirebaseAuthUserCollisionException -> {
                            // Kullanıcı zaten mevcut
                            Toast.makeText(
                                view.context,
                                "Bu e-posta adresiyle kayıtlı bir hesap zaten var.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        is FirebaseNetworkException -> {
                            // İnternet bağlantısı yok veya sunucuya erişilemiyor
                            Toast.makeText(
                                view.context,
                                "İnternet bağlantısı yok veya sunucuya erişilemiyor.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                        else -> {
                            // Diğer hatalar
                            Toast.makeText(
                                view.context,
                                "Bir hata oluştu. Lütfen daha sonra tekrar deneyin.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                }
                Resource.loading->{

                }

                else -> {}
            }

        }
    }
}