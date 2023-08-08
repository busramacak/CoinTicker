package com.bmprj.cointicker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bmprj.cointicker.R
import com.bmprj.cointicker.databinding.FragmentLoginBinding
import com.bmprj.cointicker.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_login, container,false)
        binding.login=this
        return binding.root
    }




    private fun reload(view:View){
        Toast.makeText(requireContext(),"Hoşgeldiniz, "+viewModel.currentUser?.displayName,Toast.LENGTH_LONG).show()
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_coinListFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(viewModel.currentUser!=null){
            reload(view)
        }



//        observeLiveData()

    }

    fun signUp(view:View){
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
    }
    fun login(view: View, email:String, password:String){
        viewModel.login(view,email, password)

        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_coinListFragment)
    }

    fun observeLiveData(){
        viewModel.login.observe(viewLifecycleOwner){resource->
            resource?.let {

                print(resource.toString())
            }

        }
    }


}