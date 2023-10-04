package com.bmprj.cointicker.ui.delete

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentDeleteAccountBinding
import com.bmprj.cointicker.utils.logError
import com.bmprj.cointicker.utils.navigate
import com.bmprj.cointicker.utils.setUpDialog
import com.bmprj.cointicker.utils.toast
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAccountFragment :
    BaseFragment<FragmentDeleteAccountBinding>(R.layout.fragment_delete_account) {
    private val viewModel by viewModels<DeleteAccountViewModel>()
    private val findNavController by lazy { findNavController() }
    override fun initView(view: View) {

        initLiveDataObservers()

        with(binding) {
            backButton.setOnClickListener { back() }
            logOutButton.setOnClickListener { logOut() }
            deleteAccountButton.setOnClickListener {
                deleteAccount(mailEdt.text.toString(), passwordEdt.text.toString())
            }
        }
    }

    private fun initLiveDataObservers() {

        viewModel.logOut.handleState(onSucces = {
            binding.progresBar.visibility = View.GONE
            val action =
                DeleteAccountFragmentDirections.actionDeleteAccountFragmentToLoginFragment()
            navigate(findNavController,action)
        }, onError = {
            logError(it.message)
        })
        viewModel.reEntry.handleState(onError = {
            toast(R.string.infoNotVerify)
        }, onSucces = {
            viewModel.deleteCloudData()
            toast(R.string.infoVerifySuccess)
        })

        viewModel.deleteCloud.handleState(onSucces = {
            if (it) {
                toast(R.string.deleteFavSuccess)
            } else {
                toast(R.string.deleteFavError)
            }
            viewModel.deleteStorageData()
        }, onError = {
            logError(it.message.toString())
        })

        viewModel.deleteStorage.handleState(onSucces = {
            toast(R.string.deletePhotoSuccess)
            viewModel.deleteAccount()
        }, onError = {
            toast(R.string.deletePhotoError)
            logError(it.message)
        })
        viewModel.deleteAccount.handleState(onSucces = {
            val action =
                DeleteAccountFragmentDirections.actionDeleteAccountFragmentToLoginFragment()
            navigate(findNavController,action)
            toast(R.string.deleteAccount_data)
            binding.progresBar.visibility = View.GONE
        }, onError = {
            binding.progresBar.visibility = View.GONE
            logError(it.message)
            toast(R.string.deleteAccount_dataError)
        })
    }

    private fun deleteAccount(email: String, password: String) {
        binding.progresBar.visibility = View.VISIBLE
        viewModel.reEntryUser(email, password)
    }

    private fun back() {
        val action = DeleteAccountFragmentDirections.actionDeleteAccountFragmentToCoinListFragment()
        navigate(findNavController,action)
    }

    private fun logOut() {
        val viewv = layoutInflater.inflate(R.layout.logout_dialog, null)
        val dialog = viewv.setUpDialog(requireContext())

        dialog.setOnShowListener {
            val logOutButton = viewv.findViewById<MaterialButton>(R.id.pozitiveButton)
            val cancelButton = viewv.findViewById<MaterialButton>(R.id.negativeButton)

            logOutButton.setOnClickListener {
                viewModel.logOut()
                binding.progresBar.visibility = View.VISIBLE
                dialog.dismiss()
            }
            cancelButton.setOnClickListener {
                binding.progresBar.visibility = View.GONE
                dialog.dismiss()
            }
        }
        dialog.show()
    }

}