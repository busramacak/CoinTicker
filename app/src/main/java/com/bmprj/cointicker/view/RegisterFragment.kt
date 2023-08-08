package com.bmprj.cointicker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bmprj.cointicker.R
import com.bmprj.cointicker.databinding.FragmentRegisterBinding
import com.bmprj.cointicker.view.base.BaseFragment
import com.bmprj.cointicker.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

   private val viewModel by viewModels<RegisterViewModel>()

    override fun setUpViews(view: View) {
        super.setUpViews(view)
        binding.register=this
    }

    fun login(view:View){
        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)

    }

    fun signup(view: View, name:String,email:String, password:String){
        viewModel.signup(view, name, email, password)

        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)


    }
}