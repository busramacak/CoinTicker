package com.bmprj.cointicker.ui.settings

import android.annotation.SuppressLint
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
import com.bmprj.cointicker.utils.setUpDialog
import com.bmprj.cointicker.utils.toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    private val viewModel by viewModels<SettingsViewModel>()
    private val findNavController by lazy { findNavController() }
    private lateinit var galleryLauncher :ActivityResultLauncher<Intent>
    override fun initView(view: View) {
        binding.settings=this
        initBackPress(view)
        initUserName()
        initLiveDataObservers()
        viewModel.getPhoto()
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            selectPic(it)
        }
    }

    fun backClick(){
        val action = SettingsFragmentDirections.actionSettingsFragmentToCoinListFragment()
        findNavController.navigate(action)
    }

    fun setPhotoClick(){
        selectImage()
    }

    fun userNameClick(){
        val viewv = layoutInflater.inflate(R.layout.settings_dialog,null)
        val dialog = viewv.setUpDialog(requireContext())

        dialog.setOnShowListener {
            val nameSaveButton = viewv.findViewById<MaterialTextView>(R.id.save_button)
            val nameCancelButton = viewv.findViewById<MaterialTextView>(R.id.cancel_button)
            val editText = viewv.findViewById<TextInputEditText>(R.id.nameEdt)

            editText.text?.replace(0, editText.text?.length ?: 0, viewModel.firebaseUser?.displayName)

            nameSaveButton.setOnClickListener {
                val newName = editText.text.toString()
                viewModel.changeUserName(newName)
                binding.name.text=newName
                dialog.dismiss()
            }
            nameCancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    @SuppressLint("InflateParams")
    fun passwordClick(){

        val viewv = layoutInflater.inflate(R.layout.password_change_dialog,null)
        val dialog = viewv.setUpDialog(requireContext())

        dialog.setOnShowListener {
            val saveButton = viewv.findViewById<MaterialTextView>(R.id.save_button)
            val cancelButton = viewv.findViewById<MaterialTextView>(R.id.cancel_button)
            val newPasswordEditText = viewv.findViewById<TextInputEditText>(R.id.nextPasswordEdt)
            val newPasswordConfirmEditText = viewv.findViewById<TextInputEditText>(R.id.nextPasswordConfirmEdt)

            saveButton.setOnClickListener {
                if(newPasswordEditText.text.toString() == newPasswordConfirmEditText.text.toString()){
                    viewModel.changePassword(newPasswordEditText.text.toString())
                    dialog.dismiss()
                }else{ toast(R.string.notmatch) }
            }
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun initUserName(){ binding.name.text=viewModel.firebaseUser?.displayName }

    private fun initBackPress(view: View){
        activity?.onBackPressedDispatcher?.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val action = SettingsFragmentDirections.actionSettingsFragmentToCoinListFragment()
                Navigation.findNavController(view).navigate(action)
            }
        })
    }

    private fun initLiveDataObservers(){

        viewModel.query.handleState(
            onLoading = {
                binding.settingsImageView.setImageResource(R.drawable.progres)
                binding.progresBar.visibility=View.VISIBLE
            },
            onSucces = {
                binding.settingsImageView.loadFromUrl(it.toString())
                binding.progresBar.visibility=View.GONE
            },
            onError = {
                binding.progresBar.visibility=View.GONE
                binding.settingsImageView.setImageResource(R.drawable.error)
                toast(R.string.getPhotoFail)
            }
        )

        viewModel.isSuccess.handleState(
            onLoading = {},
            onError = {
                toast(R.string.profileChangeError)
            },
            onSucces = {
                toast(R.string.profileChangeSucces)
            }
        )
    }

    private fun selectPic(result: ActivityResult){
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

    private fun selectImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }
}