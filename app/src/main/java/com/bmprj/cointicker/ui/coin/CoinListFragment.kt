package com.bmprj.cointicker.ui.coin

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.cointicker.R
import com.bmprj.cointicker.databinding.FragmentCoinListBinding
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.data.db.Entity
import com.bmprj.cointicker.model.CoinMarketItem
import com.bmprj.cointicker.utils.Resource
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

        binding.searchRecy.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.searchRecy.adapter=adapter1

        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView.editText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                
                viewModel.getDataFromDatabase(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })





        viewModel.getData()
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.coins.observe(viewLifecycleOwner){coinItem->

            when(coinItem) {
                is Resource.loading ->{

                }
                is Resource.Success ->{
                    adapter.updateList(coinItem.result)
                    viewModel.insertCoins(coinItem.result)

                }
                is Resource.Failure ->{

                }

            }
        }
        viewModel.filteredCoins.observe(viewLifecycleOwner){entity->
            entity?.let {
                adapter1.updateList(entity)
            }
        }
    }

    fun logOut(view: View){
        viewModel.logOut()
        Navigation.findNavController(view).navigate(R.id.action_coinListFragment_to_loginFragment)
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