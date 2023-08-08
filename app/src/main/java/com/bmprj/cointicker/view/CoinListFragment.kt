package com.bmprj.cointicker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bmprj.cointicker.R
import com.bmprj.cointicker.databinding.FragmentCoinListBinding
import com.bmprj.cointicker.view.base.BaseFragment

class CoinListFragment : BaseFragment<FragmentCoinListBinding>(R.layout.fragment_coin_list) {

    override fun setUpViews(view: View) {
        super.setUpViews(view)
        binding.coins=this
    }
}