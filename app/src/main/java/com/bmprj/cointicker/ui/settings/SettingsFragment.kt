package com.bmprj.cointicker.ui.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentSettingsBinding
import com.bmprj.cointicker.utils.loadFromUrl
import com.bmprj.cointicker.utils.logError
import com.bmprj.cointicker.utils.navigate
import com.bmprj.cointicker.utils.setUpDialog
import com.bmprj.cointicker.utils.toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    private val viewModel by viewModels<SettingsViewModel>()
    private val findNavController by lazy { findNavController() }
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    override fun initView(view: View) {
        with(binding) {
            backButton.setOnClickListener { backClick() }
            photoIconImageView.setOnClickListener { setPhotoClick() }
            userName.setOnClickListener { userNameClick() }
            password.setOnClickListener { passwordClick() }
        }
        initBackPress()
        initUserName()
        initLiveDataObservers()
        viewModel.getPhoto()
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                selectPic(it)
            }
    }

    private fun backClick() {
        val action = SettingsFragmentDirections.actionSettingsFragmentToCoinListFragment()
        navigate(findNavController,action)
    }

    private fun setPhotoClick() {
        selectImage()
    }

    private fun userNameClick() {
        val viewv = layoutInflater.inflate(R.layout.name_change_dialog, null)
        val dialog = viewv.setUpDialog(requireContext())

        dialog.setOnShowListener {
            val nameSaveButton = viewv.findViewById<MaterialTextView>(R.id.save_button)
            val nameCancelButton = viewv.findViewById<MaterialTextView>(R.id.cancel_button)
            val editText = viewv.findViewById<TextInputEditText>(R.id.nameEdt)

            editText.text?.replace(
                0, editText.text?.length ?: 0, viewModel.firebaseUser?.displayName
            )

            nameSaveButton.setOnClickListener {
                val newName = editText.text.toString()
                viewModel.changeUserName(newName)
                binding.name.text = newName
                dialog.dismiss()
            }
            nameCancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun passwordClick() {

        val viewv = layoutInflater.inflate(R.layout.re_entry_dialog, null)
        val dialog = viewv.setUpDialog(requireContext())

        dialog.setOnShowListener {
            val saveButton = viewv.findViewById<MaterialTextView>(R.id.save_button)
            val cancelButton = viewv.findViewById<MaterialTextView>(R.id.cancel_button)
            val emailEditText = viewv.findViewById<TextInputEditText>(R.id.eMailEdt)
            val passwordEditText = viewv.findViewById<TextInputEditText>(R.id.passwordEdt)
            saveButton.setOnClickListener {
                viewModel.reEntryUser(
                    emailEditText.text.toString(), passwordEditText.text.toString()
                )
                dialog.dismiss()
            }
            cancelButton.setOnClickListener { dialog.dismiss() }
        }
        dialog.show()
    }

    private fun showPasswordChangeDialog() {

        val viewv = layoutInflater.inflate(R.layout.password_change_dialog, null)
        val dialog = viewv.setUpDialog(requireContext())

        dialog.setOnShowListener {
            val saveButton = viewv.findViewById<MaterialTextView>(R.id.save_button)
            val cancelButton = viewv.findViewById<MaterialTextView>(R.id.cancel_button)
            val newPasswordEditText = viewv.findViewById<TextInputEditText>(R.id.nextPasswordEdt)
            val newPasswordConfirmEditText =
                viewv.findViewById<TextInputEditText>(R.id.nextPasswordConfirmEdt)

            saveButton.setOnClickListener {
                if (newPasswordEditText.text.toString() == newPasswordConfirmEditText.text.toString()) {
                    viewModel.changePassword(newPasswordEditText.text.toString())
                    dialog.dismiss()
                } else {
                    toast(R.string.notmatch)
                }
            }
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun initUserName() {
        if (viewModel.firebaseUser?.displayName == null) binding.name.text = ""
        else binding.name.text = viewModel.firebaseUser?.displayName
    }

    private fun initBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = SettingsFragmentDirections.actionSettingsFragmentToCoinListFragment()
                navigate(findNavController,action)
            }
        })
    }

    private fun initLiveDataObservers() {

        viewModel.query.handleState(onLoading = {
            binding.settingsImageView.setImageResource(R.drawable.progres)
            binding.progresBar.visibility = View.VISIBLE
        }, onSucces = {
            binding.settingsImageView.loadFromUrl(it.toString())
            binding.progresBar.visibility = View.GONE
        }, onError = {
            binding.progresBar.visibility = View.GONE
            binding.settingsImageView.setImageResource(R.drawable.error)
            toast(R.string.getPhotoFail)
        })

        viewModel.reEntry.handleState(onError = {
            toast(R.string.infoNotVerify)
            viewModel.logOut()

        }, onSucces = {
            showPasswordChangeDialog()
            toast(R.string.infoVerifySuccess)
        })

        viewModel.logOut.handleState(
            onError = {
                logError(it.message)
            },
            onSucces = {
                val action = SettingsFragmentDirections.actionSettingsFragmentToLoginFragment()
                navigate(findNavController,action)
            }
        )
        viewModel.isSuccess.handleState(onLoading = {}, onError = {
            toast(R.string.profileChangeError)
        }, onSucces = {
            toast(R.string.profileChangeSucces)
        })

        viewModel.isPasswordChange.handleState(onError = {
            toast(R.string.passwordChangeError)
        }, onSucces = {
            toast(R.string.passwordChangeSuccess)
            viewModel.logOut()
        })
    }

    private fun selectPic(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val selectedImageUri: Uri? = data.data
                if (selectedImageUri != null) {
                    binding.settingsImageView.setImageURI(selectedImageUri)
                    viewModel.changePhoto(selectedImageUri)
                }
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }
}