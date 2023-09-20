package com.bmprj.cointicker.ui.delete

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentDeleteAccountBinding
import com.bmprj.cointicker.utils.logError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAccountFragment : BaseFragment<FragmentDeleteAccountBinding>(R.layout.fragment_delete_account) {
    private val viewModel by viewModels<DeleteAccountViewModel>()
    private val findNavController by lazy { findNavController() }
    override fun initView(view: View) {
        binding.delete=this
//        initLiveDataObservers()

        logError("deneme")
    }

//    private fun initLiveDataObservers() {
//
//        viewModel.deleteCloud.handleState(
//            onLoading = {
//                toast("loading")
//            },
//            onSucces = {
//                if(it){
//                    toast("cloud fav silindi")
//                    viewModel.deleteStorageData()
//                }else{
//                    println("false")
//                }
//
//            },
//            onError = {
//                Log.e("eror",it.message.toString())
//                Log.e("eror",it.cause?.message.toString())
//            }
//        )
//
//        viewModel.deleteStorage.handleState (
//            onLoading = {
//                toast("loading")
//            },
//            onSucces = {
//                toast("storage foto silindi")
//                viewModel.deleteAccount()
//            },
//            onError = {
//                Log.e("eeerrr",it.message.toString())
//                Log.e("eror",it.localizedMessage)
//                // todo Log.kt hangi classta ve hangi fonksiyonda olduğumu öğrenmek istiyorum ama ben bunu manuel yapmak istemiyorum
//
//            }
//        )
//        viewModel.deleteAccount.handleState(
//            onLoading = {
//                toast("loading")
//            },
//            onSucces = {
//                toast("auth silindi")
//                val action = DeleteAccountFragmentDirections.actionDeleteAccountFragmentToLoginFragment()
//                findNavController.navigate(action)
//            },
//            onError = {
//                Log.e("eeerrrrrrrr",it.message.toString())
//                Log.e("erorrrrrrr",it.localizedMessage)
//            }
//        )
//    }

//    fun deleteAccount(email:String,password:String){
//        viewModel.reEntryUser(email, password)
////        viewModel.deleteCloudData()
//    }

    fun back(){

        val action = DeleteAccountFragmentDirections.actionDeleteAccountFragmentToCoinListFragment()
        findNavController.navigate(action)
    }
    fun logOut(){

    }

}