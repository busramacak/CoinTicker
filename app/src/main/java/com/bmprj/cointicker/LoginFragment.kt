package com.bmprj.cointicker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bmprj.cointicker.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_login, container,false)
        binding.login=this
        return binding.root
    }




    private fun reload(){

        Toast.makeText(requireContext(),viewModel.currentUser?.email.toString(),Toast.LENGTH_LONG).show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel = ViewModelProvider(this@LoginFragment)[LoginViewModel::class.java]
        if(viewModel.currentUser!=null){
            reload()
        }
        binding.signUp.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)

        }




        observeLiveData()

    }
    fun login(view: View, email:String, password:String){
        viewModel.login(view,email, password)
    }

    fun observeLiveData(){
        viewModel.login.observe(viewLifecycleOwner){resource->
            resource?.let {

                print(resource.toString())
            }

        }
    }


}