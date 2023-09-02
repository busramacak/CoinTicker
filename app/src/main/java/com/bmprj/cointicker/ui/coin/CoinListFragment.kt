package com.bmprj.cointicker.ui.coin

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.databinding.FragmentCoinListBinding
import com.bmprj.cointicker.domain.coin.asList
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinListFragment : BaseFragment<FragmentCoinListBinding>(R.layout.fragment_coin_list) {


    private val adapter = CoinListAdapter(onItemClicked = {item -> onCoinItemClicked(item)}, list = arrayListOf())
    private val adapter1 = SearchListAdapter(onItemClicked = {item -> onEntityItemClicked(item)},arrayListOf())
    private val viewModel by viewModels<CoinListViewModel>()




    override fun setUpViews(view: View) {
        super.setUpViews(view)
        binding.coins=this


        binding.coinListRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.coinListRecyclerView.adapter=adapter


        viewModel.getData()
        observeLiveData()
    }


    private fun observeLiveData(){
        viewModel.coins.observe(viewLifecycleOwner){coinItem->

            when(coinItem) {
                is UiState.Loading ->{
                    binding.progress.visibility=View.VISIBLE

                }
                is UiState.Success ->{
                    binding.progress.visibility=View.GONE
                    adapter.updateList(coinItem.result.asList())
                    viewModel.insertCoins(ArrayList<CoinMarketItem>().apply {
                       addAll(coinItem.result.asList())
                    })


                }
                is UiState.Error ->{
                    binding.progress.visibility=View.GONE
                    Toast.makeText(requireContext(),getString(R.string.failmsg9),Toast.LENGTH_SHORT).show()

                }

            }
        }
        viewModel.filteredCoins.observe(viewLifecycleOwner){entity->
            entity?.let {
                adapter1.updateList(entity)
            }
        }
    }



    private fun onEntityItemClicked(item: Entity) {
        val gecis = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(item.id)
        Navigation.findNavController(requireView()).navigate(gecis)
    }
    private fun onCoinItemClicked(item: CoinMarketItem) {
        val gecis = CoinListFragmentDirections.actionCoinListFragmentToCoinDetailFragment(item.id)
        Navigation.findNavController(requireView()).navigate(gecis)
    }
}