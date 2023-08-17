package com.bmprj.cointicker.view.coin

import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bmprj.cointicker.R
import com.bmprj.cointicker.data.utils.loadFromUrl
import com.bmprj.cointicker.data.utils.setArrow
import com.bmprj.cointicker.databinding.FragmentCoinDetailBinding
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.data.firebase.di.Resource
import com.bmprj.cointicker.viewmodel.CoinDetailViewModel
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinDetailFragment : BaseFragment<FragmentCoinDetailBinding>(R.layout.fragment_coin_detail){
    val bundle: CoinDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<CoinDetailViewModel>()
    private var isFav :Boolean=false
    private lateinit var coinId:String
    private lateinit var uuid:String

    override fun setUpViews(view: View) {
        super.setUpViews(view)
        binding.detail=this

        coinId= bundle.id
        uuid = bundle.userID

        println(coinId)
        println(uuid)


        viewModel.getCoin(coinId)
        viewModel.getFavourite(uuid,coinId)

        observeLiveData()




    }

    fun favClick(view:View){
        if(!isFav){
            println("fav diyildi fav oldu")
            viewModel.addFavourite(uuid,viewModel.coinDetail.value!!)
        }else{
            println("favdı fav değil oldu")
            viewModel.delete(uuid,coinId)
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
                            println("truuuu döndü")
                            binding.imageView2.setImageResource(R.drawable.fav)
                        }else{
                            binding.imageView2.setImageResource(R.drawable.empty_fav)
                            println("false döndü")
                        }

                    }
                    is Resource.Failure ->{
                        println("favoride değil")
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
                        println("Favoriden silindi")
                    }
                    is Resource.Failure ->{
                        println("Hata oluştu. silinemedi")
                    }
                    else ->{}
                }
            }
        }

        viewModel.favouriteAdd.observe(viewLifecycleOwner){favouriteAdd->
            favouriteAdd?.let {
                when(favouriteAdd){
                    is Resource.Success ->{
                        println("favoriyeEklendi")
                    }
                    is Resource.Failure ->{
                        println("Bir hata oluştu. eklenemedi")
                    }
                    else ->{

                    }
                }
            }
        }
        viewModel.coinDetail.observe(viewLifecycleOwner){ coinDetail->
            coinDetail?.let {
                binding.coinName.text=it.name
                binding.coinsymbol.text=it.symbol
                binding.imageView.loadFromUrl(it.image.thumb)
//                binding.lastUpdate.text=it.lastUpdated
//                binding.priceChange24h.text=it.marketData.priceChange24h.toString()
//                binding.arrow.setArrow(it.marketData.priceChangePercentage24h)
//                binding.precentage24hText.text= String.format("%.1f",it.marketData.priceChangePercentage24h.toString()+"%")
//                binding.lowest.text=it.marketData.low24h.usd.toString()
//                binding.highest.text=it.marketData.high24h.usd.toString()
                binding.description.text=it.description.en
            }

        }
    }


}