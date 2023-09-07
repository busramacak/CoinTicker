package com.bmprj.cointicker.ui.favourite

import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseAdapter
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.databinding.FavCoinLayoutBinding
import com.bmprj.cointicker.model.FavouriteCoin

class FavCoinListAdapter(): BaseAdapter<FavCoinLayoutBinding, FavouriteCoin>() {
    override val layoutId: Int get() = R.layout.fav_coin_layout

    private var onItemClicked: ((FavouriteCoin) -> Unit)? = null

    override fun bind(binding: FavCoinLayoutBinding, item: FavouriteCoin) {
        binding.apply {
            favCoin=item
            executePendingBindings()

            root.setOnClickListener { onItemClicked?.invoke(item) }
        }
    }

    fun setOnClickListener(onItemClicked: (FavouriteCoin) -> Unit) {
        this.onItemClicked = onItemClicked
    }


}