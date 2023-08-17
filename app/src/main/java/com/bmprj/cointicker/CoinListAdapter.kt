package com.bmprj.cointicker

import android.view.View
import androidx.navigation.Navigation
import com.bmprj.cointicker.base.BaseAdapter
import com.bmprj.cointicker.databinding.CoinListLayoutBinding
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.view.coin.CoinListFragmentDirections

class CoinListAdapter(
    private var onItemClicked:(CoinMarketItem) ->Unit,
    override var list:ArrayList<CoinMarketItem>):
    BaseAdapter<CoinListLayoutBinding, CoinMarketItem>(onItemClicked,list) {



    override val layoutId: Int
        get() = R.layout.coin_list_layout

    override fun bind(binding: CoinListLayoutBinding, item: CoinMarketItem) {
        binding.apply {
            coinList=item
            executePendingBindings()

            binding.cardV.setOnClickListener { onItemClicked(item) }

        }
    }



}