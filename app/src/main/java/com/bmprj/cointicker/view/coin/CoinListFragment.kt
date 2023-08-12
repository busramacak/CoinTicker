package com.bmprj.cointicker.view.coin

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.cointicker.CoinListAdapter
import com.bmprj.cointicker.R
import com.bmprj.cointicker.databinding.FragmentCoinListBinding
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.viewmodel.CoinListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinListFragment : BaseFragment<FragmentCoinListBinding>(R.layout.fragment_coin_list) {


    private val adapter = CoinListAdapter(arrayListOf())
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
            coinItem?.let {
                adapter.updateList(coinItem)
            }

        }
    }
}