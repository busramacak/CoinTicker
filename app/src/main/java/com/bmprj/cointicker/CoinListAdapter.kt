package com.bmprj.cointicker

import com.bmprj.cointicker.databinding.CoinListLayoutBinding
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.view.base.BaseAdapter

class CoinListAdapter(override var list:ArrayList<CoinMarketItem>):BaseAdapter<CoinListLayoutBinding,CoinMarketItem>(list) {
    override val layoutId: Int
        get() = R.layout.coin_list_layout

    override fun bind(binding: CoinListLayoutBinding, item: CoinMarketItem) {
        binding.apply {
            coinList=item
            executePendingBindings()
        }
    }
}