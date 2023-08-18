package com.bmprj.cointicker.view.adapter

import com.bmprj.cointicker.R
import com.bmprj.cointicker.view.base.BaseAdapter
import com.bmprj.cointicker.databinding.FavCoinLayoutBinding
import com.bmprj.cointicker.model.FavouriteCoin

class FavCoinListAdapter(
    private var onItemClicked:(FavouriteCoin) ->Unit,
    override var list:ArrayList<FavouriteCoin> ):
    BaseAdapter<FavCoinLayoutBinding, FavouriteCoin>(onItemClicked,list) {
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