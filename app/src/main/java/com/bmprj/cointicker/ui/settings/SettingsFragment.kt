package com.bmprj.cointicker.ui.settings

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentSettingsBinding
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.utils.loadFromUrl
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun initView(view: View) {
        binding.settings=this
        initBackPress(view)
        initUserName()
        initLiveDataObservers()
        viewModel.getPhoto()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                val selectedImageUri = data?.data
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    binding.shapeableImageView.setImageURI(selectedImageUri)
                    viewModel.changePhoto(selectedImageUri)
                }
            }
        }
    }

    fun backClick(view: View){
        Navigation.findNavController(view).navigateUp()
    }
    fun setPhotoClick(){
        selectImage()
    }

    fun userNameClick(){

        val viewv = layoutInflater.inflate(R.layout.settings_dialog,null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(viewv)
            .setCancelable(true)
            .create()

        val window: Window? = dialog.window
        val wlp = window?.attributes

        wlp?.gravity = Gravity.BOTTOM
        wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv())
        window?.attributes = wlp

        dialog.setOnShowListener {
            val nameSaveButton = viewv.findViewById<MaterialTextView>(R.id.save_button)
            val nameCancelButton = viewv.findViewById<MaterialTextView>(R.id.cancel_button)
            val editText = viewv.findViewById<TextInputEditText>(R.id.nameEdt)

            editText.text?.replace(0, editText.text?.length ?: 0, viewModel.currentUser?.displayName)

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

    fun passwordClick(){

        val viewv = layoutInflater.inflate(R.layout.password_change_dialog,null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(viewv)
            .setCancelable(true)
            .create()

        val window: Window? = dialog.window
        val wlp = window?.attributes

        wlp?.gravity = Gravity.BOTTOM
        wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv())
        window?.attributes = wlp

        dialog.setOnShowListener {
            val saveButton = viewv.findViewById<MaterialTextView>(R.id.save_button)
            val cancelButton = viewv.findViewById<MaterialTextView>(R.id.cancel_button)
            val newPasswordEditText = viewv.findViewById<TextInputEditText>(R.id.nextPEdt)
            val newPasswordConfirmEditText = viewv.findViewById<TextInputEditText>(R.id.nextPOEdt)

            saveButton.setOnClickListener {
                if(newPasswordEditText.text.toString() == newPasswordConfirmEditText.text.toString()){
                    viewModel.changePassword(newPasswordEditText.text.toString())
                    dialog.dismiss()
                }else{
                    Toast.makeText(requireContext(),getString(R.string.notmatch),Toast.LENGTH_SHORT).show()
                }
            }
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun initUserName(){ binding.name.text=viewModel.currentUser?.displayName }

    private fun initBackPress(view: View){
        activity?.onBackPressedDispatcher?.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.findNavController(view).navigateUp()
            }
        })
    }

    private fun initLiveDataObservers(){
        viewModel.query.observe(viewLifecycleOwner){resource->
            when(resource){
                is Resource.loading ->{
                    binding.progress.visibility=View.VISIBLE
                }
                is Resource.Success -> {
                    binding.shapeableImageView.loadFromUrl(resource.result.toString())
                    binding.progress.visibility=View.GONE
                }
                is Resource.Failure -> {
                    binding.progress.visibility=View.GONE
                    Toast.makeText(requireContext(),getString(R.string.getPhotoFail),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun selectImage(){
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200)
    }
}