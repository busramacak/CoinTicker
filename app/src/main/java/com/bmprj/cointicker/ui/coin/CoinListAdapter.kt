package com.bmprj.cointicker.ui.coin

import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseAdapter
import com.bmprj.cointicker.databinding.CoinListLayoutBinding
import com.bmprj.cointicker.model.CoinMarketItem

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