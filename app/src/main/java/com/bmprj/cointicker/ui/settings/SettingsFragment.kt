package com.bmprj.cointicker.ui.settings

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
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

    override fun setUpViews(view: View) {
        super.setUpViews(view)

        binding.settings=this
        binding.name.text=viewModel.currentUser?.displayName

        viewModel.getPhoto()
        observeLiveData()
    }

    private fun observeLiveData(){
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

    fun backClick(view: View){
        Navigation.findNavController(view).navigateUp()

    }


    fun setPhotoClick(){
        selectImage()
    }

    private fun selectImage(){
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200)

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

    fun userNameClick(view:View){

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
            val btnp = viewv.findViewById<MaterialTextView>(R.id.save_button)
            val btnn = viewv.findViewById<MaterialTextView>(R.id.cancel_button)
            val editText = viewv.findViewById<TextInputEditText>(R.id.nameEdt)

            editText.text?.replace(0, editText.text?.length ?: 0, viewModel.currentUser?.displayName)

            btnp.setOnClickListener {
                val newName = editText.text.toString()
                viewModel.changeUserName(newName)
                binding.name.text=newName
                dialog.dismiss()
            }
            btnn.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()


    }


    fun passwordClick(view:View){

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
            val btnp = viewv.findViewById<MaterialTextView>(R.id.save_button)
            val btnn = viewv.findViewById<MaterialTextView>(R.id.cancel_button)
            val nextP = viewv.findViewById<TextInputEditText>(R.id.nextPEdt)
            val nextPO = viewv.findViewById<TextInputEditText>(R.id.nextPOEdt)


            btnp.setOnClickListener {

                if(nextP.text.toString() == nextPO.text.toString()){
                    viewModel.changePassword(nextP.text.toString())
                    dialog.dismiss()
                }else{
                    Toast.makeText(requireContext(),"Yeni şifre ve şifre onayı birbirine uymuyor.",Toast.LENGTH_SHORT).show()
                }


            }
            btnn.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }

}