package com.bmprj.cointicker.ui.favourite

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentFavCoinsBinding
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.model.FavouriteCoin
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavCoinsFragment : BaseFragment<FragmentFavCoinsBinding>(R.layout.fragment_fav_coins) {

    private val adapter= FavCoinListAdapter(onItemClicked = {item -> onCoinItemClicked(item)}, arrayListOf())
    private val viewModel by viewModels<FavCoinsViewModel>()
    override fun setUpViews(view: View) {
        super.setUpViews(view)
        binding.fav=this

        binding.favCoinListRecyclerView.adapter=adapter
        binding.favCoinListRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        viewModel.getFavCoins()

        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.favCoins.observe(viewLifecycleOwner){resource->
            when(resource){
                is Resource.loading ->{
                    binding.progress.visibility=View.VISIBLE
                }
                is Resource.Success ->{
                    binding.progress.visibility=View.GONE
                    adapter.updateList(ArrayList(resource.result))
                }
                is Resource.Failure ->{
                    binding.progress.visibility=View.GONE
                    //TODO fail dialog eklenecek
                    Log.e("exception",resource.exception.message!!)
                }
            }
        }
    }


    private fun onCoinItemClicked(item: FavouriteCoin) {
        val gecis = FavCoinsFragmentDirections.actionFavCoinsFragmentToCoinDetailFragment(item.id)
        Navigation.findNavController(requireView()).navigate(gecis)
    }
}