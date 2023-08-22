package com.bmprj.cointicker.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bmprj.cointicker.R
import com.bumptech.glide.Glide


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
