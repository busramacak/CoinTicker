package com.bmprj.cointicker.ui.delete

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentDeleteAccountBinding
import com.bmprj.cointicker.utils.logError
import com.bmprj.cointicker.utils.setUpDialog
import com.bmprj.cointicker.utils.toast
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAccountFragment : BaseFragment<FragmentDeleteAccountBinding>(R.layout.fragment_delete_account) {
    private val viewModel by viewModels<DeleteAccountViewModel>()
    private val findNavController by lazy { findNavController() }
    override fun initView(view: View) {
        binding.delete=this
        initLiveDataObservers()

        logError("deneme")
    }

    private fun initLiveDataObservers() {


        viewModel.logOut.handleState(
            onLoading = {
                binding.progresBar.visibility=View.VISIBLE
            },
            onSucces = {
                binding.progresBar.visibility=View.GONE
            },
            onError = {
                binding.progresBar.visibility=View.GONE
                logError(it.message)
            }
        )
        viewModel.reEntry.handleState(
            onLoading = {
                binding.progresBar.visibility=View.VISIBLE
                toast("Bilgileriniz kontrol ediliyor.")
            },
            onError = {
                toast("Bilgilerinizi doğrulayamadık. lütfen tekrar giriş yapın.")
            },
            onSucces = {
                binding.progresBar.visibility=View.GONE
                viewModel.deleteCloudData()
                toast("kimlik doğrulama başarılı.")
            }
        )

        viewModel.deleteCloud.handleState(
            onLoading = {
                binding.progresBar.visibility=View.VISIBLE
            },
            onSucces = {
                if(it){
                    toast("favorileri silme başarılı.")
                    binding.progresBar.visibility=View.GONE
                    viewModel.deleteStorageData()
                }else{
                    logError("cloud silme işlemi başarısız")
                }
            },
            onError = {
                binding.progresBar.visibility=View.GONE
                Log.e("eror",it.message.toString())
                Log.e("eror",it.cause?.message.toString())
            }
        )

        viewModel.deleteStorage.handleState (
            onLoading = {
                binding.progresBar.visibility=View.VISIBLE
            },
            onSucces = {
                toast("profil fotografı silme başarılı")
                binding.progresBar.visibility=View.GONE
                viewModel.deleteAccount()
            },
            onError = {
                toast("profil fotoğrafı silinemedi.. Daha sonra tekrar deneyin")
                binding.progresBar.visibility=View.GONE
                logError(it.message)
                // todo Log.kt hangi classta ve hangi fonksiyonda olduğumu öğrenmek istiyorum ama ben bunu manuel yapmak istemiyorum
            }
        )
        viewModel.deleteAccount.handleState(
            onLoading = {
                binding.progresBar.visibility=View.VISIBLE
            },
            onSucces = {
                val action = DeleteAccountFragmentDirections.actionDeleteAccountFragmentToLoginFragment()
                findNavController.navigate(action)
                binding.progresBar.visibility=View.GONE
            },
            onError = {
                binding.progresBar.visibility=View.GONE
               logError(it.message)
            }
        )
    }

    fun deleteAccount(email:String,password:String){
        viewModel.reEntryUser(email,password)
    }

    fun back(){
        val action = DeleteAccountFragmentDirections.actionDeleteAccountFragmentToCoinListFragment()
        findNavController.navigate(action)
    }
    fun logOut(){
        val viewv = layoutInflater.inflate(R.layout.logout_dialog, null)
        val dialog = viewv.setUpDialog(requireContext())

        dialog.setOnShowListener {
            val logOutButton = viewv.findViewById<MaterialButton>(R.id.pozitiveButton)
            val cancelButton = viewv.findViewById<MaterialButton>(R.id.negativeButton)

            logOutButton.setOnClickListener {
                viewModel.logOut()
                dialog.dismiss()
            }
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

}