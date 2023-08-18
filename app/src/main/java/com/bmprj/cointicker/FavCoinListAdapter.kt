package com.bmprj.cointicker

import com.bmprj.cointicker.base.BaseAdapter
import com.bmprj.cointicker.databinding.FavCoinLayoutBinding
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.model.FavouriteCoin

class FavCoinListAdapter(
    private var onItemClicked:(FavouriteCoin) ->Unit,
    override var list:ArrayList<FavouriteCoin> ):BaseAdapter<FavCoinLayoutBinding,FavouriteCoin>(onItemClicked,list) {
    override val layoutId: Int
        get() = R.layout.fav_coin_layout

    override fun bind(binding: FavCoinLayoutBinding, item: FavouriteCoin) {
        binding.apply {
            favCoin=item
            binding.executePendingBindings()

            binding.cardV.setOnClickListener { onItemClicked(item) }
        }
    }


}