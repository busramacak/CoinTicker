package com.bmprj.cointicker.view.coin

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.cointicker.CoinListAdapter
import com.bmprj.cointicker.R
import com.bmprj.cointicker.SearchListAdapter
import com.bmprj.cointicker.databinding.FragmentCoinListBinding
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.viewmodel.CoinListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinListFragment : BaseFragment<FragmentCoinListBinding>(R.layout.fragment_coin_list) {


    private val adapter = CoinListAdapter(arrayListOf())
    private val adapter1 = SearchListAdapter(arrayListOf())
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


        viewModel.filteredCoins.observe(viewLifecycleOwner){entity->
            entity?.let {
                adapter1.updateList(entity)
            }
        }


        viewModel.getData()
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.coins.observe(viewLifecycleOwner){coinItem->
            coinItem?.let {
                adapter.updateList(coinItem)
                viewModel.insertCoins(coinItem)
            }

        }
    }
}