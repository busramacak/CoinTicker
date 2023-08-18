package com.bmprj.cointicker.view.coin

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.cointicker.view.adapter.FavCoinListAdapter
import com.bmprj.cointicker.R
import com.bmprj.cointicker.view.base.BaseFragment
import com.bmprj.cointicker.data.remote.firebase.di.Resource
import com.bmprj.cointicker.databinding.FragmentFavCoinsBinding
import com.bmprj.cointicker.model.FavouriteCoin
import com.bmprj.cointicker.viewmodel.FavCoinsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavCoinsFragment : BaseFragment<FragmentFavCoinsBinding>(R.layout.fragment_fav_coins) {

    private lateinit var adapter : FavCoinListAdapter
    private val viewModel by viewModels<FavCoinsViewModel>()
    override fun setUpViews(view: View) {
        super.setUpViews(view)
        binding.fav=this



        adapter= FavCoinListAdapter(
            onItemClicked = {item -> onCoinItemClicked(item)},
            arrayListOf()
        )

        binding.favCoinListRecyclerView.adapter=adapter
        binding.favCoinListRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        viewModel.getFavCoins()

        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.favCoins.observe(viewLifecycleOwner){resource->
            when(resource){
                is Resource.Success ->{
                    adapter.updateList(ArrayList(resource.result))
                }
                is Resource.Failure ->{
                    println(resource.exception.message)
                }
                else ->{}
            }
        }
    }


    private fun onCoinItemClicked(item: FavouriteCoin) {

        val gecis = FavCoinsFragmentDirections.actionFavCoinsFragmentToCoinDetailFragment(item.id)
        println(item.id)
        Navigation.findNavController(requireView()).navigate(gecis)
    }



}