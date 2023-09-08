package com.bmprj.cointicker.ui.coin

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentCoinListBinding
import com.bmprj.cointicker.domain.coin.asList
import com.bmprj.cointicker.model.CoinMarketItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoinListFragment : BaseFragment<FragmentCoinListBinding>(R.layout.fragment_coin_list) {


    private val adapter by lazy { CoinListAdapter() }
    private val viewModel by viewModels<CoinListViewModel>()


    override fun initView(view: View) {
        initAdapter()
        initLiveDataObservers()
        viewModel.getData()
    }

    private fun initAdapter(){
        binding.coinListRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.coinListRecyclerView.adapter=adapter

        adapter.setOnClickListener { onCoinItemClicked(it) }
    }


    private fun initLiveDataObservers(){

        lifecycleScope.launch {
            viewModel.coins.handleState (
                onLoading = {
                    binding.progress.visibility=View.VISIBLE
                },
                onSucces = {
                    binding.progress.visibility=View.GONE
                    adapter.updateList(it.asList())
                    viewModel.insertCoins(ArrayList<CoinMarketItem>().apply {
                        addAll(it.asList())
                    })
                },
                onError = {
                    binding.progress.visibility=View.GONE
                    Toast.makeText(requireContext(),getString(R.string.failmsg9),Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun onCoinItemClicked(item: CoinMarketItem) {
        val transition = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(item.id)
        Navigation.findNavController(requireView()).navigate(transition)
    }
}