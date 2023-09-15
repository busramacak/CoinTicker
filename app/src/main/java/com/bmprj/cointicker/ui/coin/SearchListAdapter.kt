package com.bmprj.cointicker.ui.coin

import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseAdapter
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.databinding.SearchListLayoutBinding

class SearchListAdapter(): BaseAdapter<SearchListLayoutBinding, Entity>() {
    override val layoutId: Int get() = R.layout.search_list_layout
    private var onItemClicked: ((Entity) -> Unit)? = null

    override fun bind(binding: SearchListLayoutBinding, item: Entity) {
        with(binding) {
            coinList=item
            executePendingBindings()

            root.setOnClickListener{ onItemClicked?.invoke(item) }
        }
    }

    fun setOnClickListener(onItemClicked: (Entity) -> Unit) {
        this.onItemClicked = onItemClicked
    }
}