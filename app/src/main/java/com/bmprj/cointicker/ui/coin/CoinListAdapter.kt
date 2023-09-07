package com.bmprj.cointicker.ui.coin

import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseAdapter
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.databinding.CoinListLayoutBinding
import com.bmprj.cointicker.model.CoinMarketItem

class CoinListAdapter():BaseAdapter<CoinListLayoutBinding, CoinMarketItem>() {

    override val layoutId: Int get() = R.layout.coin_list_layout
    private var onItemClicked: ((CoinMarketItem) -> Unit)? = null

    override fun bind(binding: CoinListLayoutBinding, item: CoinMarketItem) {
        binding.apply {
            coinList=item
            executePendingBindings()

            root.setOnClickListener {
                onItemClicked?.invoke(item)
            }

        }
    }

    fun setOnClickListener(onItemClicked: (CoinMarketItem) -> Unit) {
        this.onItemClicked = onItemClicked
    }

}