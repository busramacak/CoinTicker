package com.bmprj.cointicker.ui.favourite

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentFavCoinsBinding
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.model.FavouriteCoin
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FavCoinsFragment : BaseFragment<FragmentFavCoinsBinding>(R.layout.fragment_fav_coins) {

    private val adapter by lazy { FavCoinListAdapter() }
    private val viewModel by viewModels<FavCoinsViewModel>()

    override fun initView(view: View) {
        initAdapter()
        initLiveDataObservers()
        viewModel.getFavCoins()
    }
    
    private fun initAdapter(){
        binding.favCoinListRecyclerView.adapter=adapter
        binding.favCoinListRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
    
        adapter.setOnClickListener { onCoinItemClicked(it) }
    }

    private fun initLiveDataObservers(){

        lifecycleScope.launch {
            viewModel.favCoins.handleState(
                onLoading = {
                    binding.progress.visibility=View.VISIBLE
                },
                onSucces = {
                    binding.progress.visibility=View.GONE
                    adapter.updateList(ArrayList(it))
                },
                onError = {
                    binding.progress.visibility=View.GONE
                    //TODO fail dialog eklenecek
                    Log.e("exception",it.message!!)
                }
            )
        }
    }
    private fun onCoinItemClicked(item: FavouriteCoin) {
        val transition = FavCoinsFragmentDirections.actionFavCoinsFragmentToCoinDetailFragment(item.id)
        Navigation.findNavController(requireView()).navigate(transition)
    }
}