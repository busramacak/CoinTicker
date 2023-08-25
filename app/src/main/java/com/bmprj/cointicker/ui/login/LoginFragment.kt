package com.bmprj.cointicker.ui.login

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bmprj.cointicker.R
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.databinding.FragmentLoginBinding
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.utils.FirebaseAuthError
import com.bmprj.cointicker.utils.UiState
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun setUpViews(view: View) {
        super.setUpViews(view)
        binding.login=this

        observeLiveData(view)

        if(viewModel.currentUser!=null){
            reload(view)
        }
    }



    private fun reload(view:View){
        Toast.makeText(requireContext(),"Hoşgeldiniz, "+viewModel.currentUser?.displayName,Toast.LENGTH_LONG).show()
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_coinListFragment)
    }


    fun signUp(view:View){
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun login(email:String, password:String){

            viewModel.login(email, password)
    }



    fun observeLiveData(view:View){
        viewModel.login.observe(viewLifecycleOwner){resource->
            when(resource){
                is UiState.Loading->{
                    binding.progress.visibility=View.VISIBLE
                }
                is UiState.Success->{
                    binding.progress.visibility=View.GONE
                    reload(view)
                }
                is UiState.Error -> {
                    binding.progress.visibility=View.GONE
                    when (resource.error) {
                        is FirebaseAuthInvalidUserException -> {
                            // Kullanıcı bulunamadı veya etkin değil
                            Toast.makeText(view.context,getString(R.string.failmsg1),Toast.LENGTH_LONG).show()
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            // Geçersiz kimlik bilgileri
                            Toast.makeText(view.context,getString(R.string.failmsg2),Toast.LENGTH_LONG).show()

                        }

                        is FirebaseNetworkException -> {
                            // İnternet bağlantısı yok veya sunucuya erişilemiyor
                            Toast.makeText(view.context,getString(R.string.failmsg3),Toast.LENGTH_LONG).show()

                        }

                        else -> {
                            // Diğer hatalar
                            Toast.makeText(view.context,getString(R.string.failmsg4),Toast.LENGTH_LONG).show()

                        }
                    }
                }

                else -> {}
            }

        }
    }


}


