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
import com.bmprj.cointicker.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : Fragment() {

   private lateinit var binding:FragmentRegisterBinding
   private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.register=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        

//        observeLiveData()
    }

//    private fun observeLiveData(){
//
//    }

    fun login(view:View){
        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)

    }

    fun signup(view: View, name:String,email:String, password:String){
        viewModel.signup(view, name, email, password)

        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)


    }
}