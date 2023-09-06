package com.bmprj.cointicker.ui.login

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentLoginBinding
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
        initTextType()

        if(viewModel.currentUser!=null){
            reload(view)
        }


    }

    fun signUp(view:View){
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun login(email:String, password:String){

        viewModel.login(email, password)
    }
    fun openCoinGecko(){

        val uri = Uri.parse("https://www.coingecko.com/tr/api") // missing 'http://' will cause crashed

        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun reload(view:View){
        Toast.makeText(requireContext(),getString(R.string.welcome,viewModel.currentUser?.displayName),Toast.LENGTH_LONG).show()
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_coinListFragment)
    }

    private fun observeLiveData(view:View){
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

    private fun initTextType(){
        if(getString(R.string.locale)=="tr"){
            binding.materialTextView4.typeface=Typeface.defaultFromStyle(Typeface.BOLD)
            binding.materialTextView4.setTextColor(resources.getColor(R.color.textColor1))

            binding.materialTextView5.typeface=Typeface.defaultFromStyle(Typeface.NORMAL)
            binding.materialTextView5.setTextColor(resources.getColor(R.color.textColor2))
        }else{
            binding.materialTextView4.typeface=Typeface.defaultFromStyle(Typeface.NORMAL)
            binding.materialTextView4.setTextColor(resources.getColor(R.color.textColor2))

            binding.materialTextView5.typeface=Typeface.defaultFromStyle(Typeface.BOLD)
            binding.materialTextView5.setTextColor(resources.getColor(R.color.textColor1))
        }
    }
}


