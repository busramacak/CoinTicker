package com.bmprj.cointicker.ui.coin

import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseAdapter
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.databinding.SearchListLayoutBinding

class SearchListAdapter(
    private var onItemClicked:(Entity) ->Unit,
    override var list:ArrayList<Entity>):
    BaseAdapter<SearchListLayoutBinding, Entity>(onItemClicked,list) {
    override val layoutId: Int
        get() = R.layout.search_list_layout

    override fun bind(binding: SearchListLayoutBinding, item: Entity) {
        binding.apply {
            coinList=item
            executePendingBindings()

//            binding.cardV.setOnClickListener {
//                cardVClick(binding.root,binding.coinList!!.id)
//            }
        }
    }

//    fun cardVClick(view: View, id:String){
//        val gecis = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(id)
//        Navigation.findNavController(view).navigate(gecis)
//    }
}