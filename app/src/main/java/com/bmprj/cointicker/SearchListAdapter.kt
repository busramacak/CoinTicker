package com.bmprj.cointicker

import android.view.View
import androidx.navigation.Navigation
import com.bmprj.cointicker.base.BaseAdapter
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.databinding.SearchListLayoutBinding
import com.bmprj.cointicker.view.coin.CoinListFragmentDirections

class SearchListAdapter(override var list:ArrayList<Entity>):
    BaseAdapter<SearchListLayoutBinding, Entity>(list) {
    override val layoutId: Int
        get() = R.layout.search_list_layout

    override fun bind(binding: SearchListLayoutBinding, item: Entity) {
        binding.apply {
            coinList=item
            executePendingBindings()

            binding.cardV.setOnClickListener {
                cardVClick(binding.root,binding.coinList!!.id)
            }
        }
    }

    fun cardVClick(view: View, id:String){
        val gecis = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(id)
        Navigation.findNavController(view).navigate(gecis)
    }
}