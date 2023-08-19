package com.bmprj.cointicker.view.coin


import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bmprj.cointicker.R
import com.bmprj.cointicker.data.utils.loadFromUrl
import com.bmprj.cointicker.databinding.FragmentCoinDetailBinding
import com.bmprj.cointicker.view.base.BaseFragment
import com.bmprj.cointicker.data.remote.firebase.di.Resource
import com.bmprj.cointicker.data.utils.setArrow
import com.bmprj.cointicker.viewmodel.CoinDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinDetailFragment : BaseFragment<FragmentCoinDetailBinding>(R.layout.fragment_coin_detail){
    val bundle: CoinDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<CoinDetailViewModel>()
    private var isFav :Boolean=false
    private lateinit var coinId:String

    override fun setUpViews(view: View) {
        super.setUpViews(view)
        binding.detail=this

        coinId= bundle.id

        println(coinId)


        viewModel.getCoin(coinId)
        viewModel.getFavourite(coinId)

        observeLiveData()




    }

    fun favClick(){
        if(!isFav){
            viewModel.addFavourite(viewModel.coinDetail.value!!)
        }else{
            viewModel.delete(coinId)
        }

        isFav=!isFav
        viewModel.isFavourite.value = Resource.Success(isFav)
    }

    private fun observeLiveData(){

        viewModel.isFavourite.observe(viewLifecycleOwner){isFavourite->
            isFavourite?.let {
                when(isFavourite){
                    is Resource.Success ->{
                        isFav = isFavourite.result
                        if(isFavourite.result){
                            binding.imageView2.setImageResource(R.drawable.fav)
                        }else{
                            binding.imageView2.setImageResource(R.drawable.empty_fav)
                        }

                    }
                    is Resource.Failure ->{
                        isFav=false
                        binding.imageView2.setImageResource(R.drawable.empty_fav)

                    }
                    else->{}
                }
            }
        }

        viewModel.favouriteDelete.observe(viewLifecycleOwner){favDelete->
            favDelete?.let {
                when(favDelete){
                    is Resource.Success ->{
                        Toast.makeText(context,getString(R.string.delFavSuccess),Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Failure ->{
                        Toast.makeText(context,getString(R.string.delFavFail),Toast.LENGTH_SHORT).show()
                    }
                    else ->{}
                }
            }
        }

        viewModel.favouriteAdd.observe(viewLifecycleOwner){favouriteAdd->
            favouriteAdd?.let {
                when(favouriteAdd){
                    is Resource.Success ->{
                        Toast.makeText(context,getString(R.string.addFavSuccess),Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Failure ->{
                        Toast.makeText(context,getString(R.string.addFavFail),Toast.LENGTH_SHORT).show()
                    }
                    else ->{}
                }
            }
        }
        viewModel.coinDetail.observe(viewLifecycleOwner){ coinDetail->
            coinDetail?.let {
                binding.coinName.text=it.name
                binding.coinsymbol.text=it.symbol
                binding.imageView.loadFromUrl(it.image.large)
                binding.lastUpdate.text=it.lastUpdated
                binding.priceChange24h.text=it.marketData.currentPrice.usd.toDouble().toString() + "$"
                binding.arrow.setArrow(it.marketData.priceChangePercentage24h)
                binding.precentage24hText.text= String.format("%.1f",it.marketData.priceChangePercentage24h.toFloat())+"%"
                binding.lowest.text=it.marketData.low24h.usd.toDouble().toString()
                binding.highest.text=it.marketData.high24h.usd.toDouble().toString()
                binding.description.text=it.description.en
            }

        }
    }

    fun backButton(view: View){
        Navigation.findNavController(view).navigateUp()
    }
}