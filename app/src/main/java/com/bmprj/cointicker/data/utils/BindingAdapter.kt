package com.bmprj.cointicker.data.utils

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bmprj.cointicker.R
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("edtError")
fun TextInputLayout.edtError(str:String){
    if(str.isNullOrEmpty()){
        this.edtError(this.hint.toString()+" Boş Bırakılamaz..")

    }
}


    @BindingAdapter("loadFromUrl")
    fun ImageView.loadFromUrl(imgUrl:String){
        imgUrl.let{
            Glide.with(this)
                .load(it.toUri().buildUpon().scheme("https").build())
                .into(this)
        }
    }

    @BindingAdapter("setPriceBackground")
    fun TextView.setPriceBackground(d:Double){
        this.setBackgroundResource(
            if(d>0) R.drawable.coin_price_up
            else R.drawable.coin_price_down
        )
    }

    @BindingAdapter("setArrow")
    fun ImageView.setArrow(d:Double){
        this.setImageResource(
            if(d>0) R.drawable.up
            else R.drawable.down
        )
    }
