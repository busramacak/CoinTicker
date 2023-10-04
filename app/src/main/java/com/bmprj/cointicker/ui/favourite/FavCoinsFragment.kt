package com.bmprj.cointicker.ui.favourite

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentFavCoinsBinding
import com.bmprj.cointicker.model.FavouriteCoin
import com.bmprj.cointicker.utils.Constants
import com.bmprj.cointicker.utils.logError
import com.bmprj.cointicker.utils.navigate
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavCoinsFragment : BaseFragment<FragmentFavCoinsBinding>(R.layout.fragment_fav_coins) {

    private val favCoinListAdapter by lazy { FavCoinListAdapter() }
    private val viewModel by viewModels<FavCoinsViewModel>()
    private val findNavController by lazy { findNavController() }

    override fun initView(view: View) {
        initAdapter()
        initLiveDataObservers()
        viewModel.getFavCoins()
    }

    private fun initAdapter() {
        with(binding) {
            favCoinListRecyclerView.adapter = favCoinListAdapter
            favCoinListRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        favCoinListAdapter.setOnClickListener { onCoinItemClicked(it) }
    }

    private fun initLiveDataObservers() {

        viewModel.favCoins.handleState(onLoading = {
            binding.progresBar.visibility = View.VISIBLE
        }, onSucces = {
            binding.progresBar.visibility = View.GONE
            favCoinListAdapter.updateList(ArrayList(it))
        }, onError = {
            binding.progresBar.visibility = View.GONE
            logError(it.message)
        })
    }

    private fun onCoinItemClicked(item: FavouriteCoin) {
        val action = FavCoinsFragmentDirections.actionFavCoinsFragmentToCoinDetailFragment(
            item.id, Constants.COLLECTION_FAV
        )
        navigate(findNavController, action)
    }
}