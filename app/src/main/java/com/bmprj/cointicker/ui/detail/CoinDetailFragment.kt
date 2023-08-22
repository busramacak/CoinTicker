package com.bmprj.cointicker.ui.detail


import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bmprj.cointicker.R
import com.bmprj.cointicker.utils.loadFromUrl
import com.bmprj.cointicker.databinding.FragmentCoinDetailBinding
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.model.CoinDetail
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.utils.setArrow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinDetailFragment : BaseFragment<FragmentCoinDetailBinding>(R.layout.fragment_coin_detail){
    val bundle: CoinDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<CoinDetailViewModel>()
    private var isFav :Boolean=false
    private lateinit var coinId:String
    private lateinit var coindetail :CoinDetail

    override fun setUpViews(view: View) {
        super.setUpViews(view)
        binding.detail=this

        coinId= bundle.id


        viewModel.getCoin(coinId)
        viewModel.getFavourite(coinId)

        observeLiveData()




    }

    fun favClick(){
        if(!isFav){
            viewModel.addFavourite(coindetail)
        }else{
            viewModel.delete(coinId)
        }

        isFav=!isFav
        viewModel.isFavourite.value = Resource.Success(isFav)
    }

    private fun observeLiveData(){

        viewModel.isFavourite.observe(viewLifecycleOwner){isFavourite->
                when(isFavourite){
                    is Resource.loading ->{
                        print("loading")
                    }
                    is Resource.Success ->{
                        print(isFavourite.result)
                        isFav = isFavourite.result
                        if(isFavourite.result){
                            binding.imageView2.setImageResource(R.drawable.fav)
                        }else{
                            binding.imageView2.setImageResource(R.drawable.empty_fav)
                        }

                    }
                    is Resource.Failure ->{
                        print(isFavourite.exception.message)
                        isFav=false
                        binding.imageView2.setImageResource(R.drawable.empty_fav)

                    }
                }

        }

        viewModel.favouriteDelete.observe(viewLifecycleOwner){favDelete->
            favDelete?.let {
                when(favDelete){
                    is Resource.loading ->{

                    }
                    is Resource.Success ->{
                        Toast.makeText(context,getString(R.string.delFavSuccess),Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Failure ->{
                        Toast.makeText(context,getString(R.string.delFavFail),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.favouriteAdd.observe(viewLifecycleOwner){favouriteAdd->
            favouriteAdd?.let {
                when(favouriteAdd){
                    is Resource.loading ->{

                    }
                    is Resource.Success ->{
                        Toast.makeText(context,getString(R.string.addFavSuccess),Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Failure ->{
                        Toast.makeText(context,getString(R.string.addFavFail),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        viewModel.coinDetail.observe(viewLifecycleOwner){ coinDetail->
            when(coinDetail) {
                is Resource.loading ->{

                }
                is Resource.Success ->{
                    coindetail=coinDetail.result
                    binding.coinName.text=coinDetail.result.name
                    binding.coinsymbol.text=coinDetail.result.symbol
                    binding.imageView.loadFromUrl(coinDetail.result.image.large)
                    binding.lastUpdate.text=coinDetail.result.lastUpdated
                    binding.priceChange24h.text=getString(R.string.priceChange24hText,coinDetail.result.marketData.currentPrice.usd.toString())
                    binding.arrow.setArrow(coinDetail.result.marketData.priceChangePercentage24h)
                    binding.precentage24hText.text= getString(R.string.precentage24hText,coinDetail.result.marketData.priceChangePercentage24h.toFloat())
                    binding.lowest.text=coinDetail.result.marketData.low24h.usd.toString()
                    binding.highest.text=coinDetail.result.marketData.high24h.usd.toString()
                    binding.description.text=coinDetail.result.description.en

                }
                is Resource.Failure ->{

                }


            }

        }
    }

    fun backButton(view: View){
        Navigation.findNavController(view).navigateUp()
    }
}