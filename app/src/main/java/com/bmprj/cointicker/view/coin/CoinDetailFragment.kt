package com.bmprj.cointicker.view.coin

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bmprj.cointicker.R
import com.bmprj.cointicker.data.utils.loadFromUrl
import com.bmprj.cointicker.data.utils.setArrow
import com.bmprj.cointicker.databinding.FragmentCoinDetailBinding
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.viewmodel.CoinDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinDetailFragment : BaseFragment<FragmentCoinDetailBinding>(R.layout.fragment_coin_detail){
    val bundle: CoinDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<CoinDetailViewModel>()

    override fun setUpViews(view: View) {
        super.setUpViews(view)
//        binding.detail=this

        val id = bundle.id
        println(id)

        viewModel.getCoin(id)

//        observeLiveData()




    }

    private fun observeLiveData(){
        println("observe içinde")
        viewModel.coinDetail.observe(viewLifecycleOwner){ coinDetail->
            coinDetail?.let {
                binding.coinName.text=it.name
                binding.coinsymbol.text=it.symbol
                binding.imageView.loadFromUrl(it.image.thumb)
                binding.lastUpdate.text=it.lastUpdated
                binding.priceChange24h.text=it.marketData.priceChange24h.toString()
                binding.arrow.setArrow(it.marketData.priceChangePercentage24h)
                binding.precentage24hText.text= String.format("%.1f",it.marketData.priceChangePercentage24h.toString()+"%")
                binding.lowest.text=it.marketData.low24h.usd.toString()
                binding.highest.text=it.marketData.high24h.usd.toString()
                binding.description.text=it.description.en
            }

        }
    }


}