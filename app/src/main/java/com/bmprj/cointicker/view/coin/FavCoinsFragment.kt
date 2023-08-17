package com.bmprj.cointicker.view.coin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bmprj.cointicker.FavCoinListAdapter
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentFavCoinsBinding
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.model.FavouriteCoin
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavCoinsFragment : BaseFragment<FragmentFavCoinsBinding>(R.layout.fragment_fav_coins) {

    private lateinit var adapter : FavCoinListAdapter
    override fun setUpViews(view: View) {
        super.setUpViews(view)
        binding.fav=this

        adapter= FavCoinListAdapter(
            onItemClicked = {item -> onCoinItemClicked(item)},
            arrayListOf()
        )
    }


    private fun onCoinItemClicked(item: FavouriteCoin) {
//        val gecis = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(uuid,item.id)
//        println(item.id)
//        Navigation.findNavController(requireView()).navigate(gecis)
    }



}