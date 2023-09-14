package com.bmprj.cointicker.ui.detail


import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentCoinDetailBinding
import com.bmprj.cointicker.domain.coins.CoinDetailEntity
import com.bmprj.cointicker.domain.coins.asCoinDetail
import com.bmprj.cointicker.utils.Constants
import com.bmprj.cointicker.utils.UiState
import com.bmprj.cointicker.utils.fixedString
import com.bmprj.cointicker.utils.loadFromUrl
import com.bmprj.cointicker.utils.setArrow
import com.bmprj.cointicker.utils.setDateTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CoinDetailFragment : BaseFragment<FragmentCoinDetailBinding>(R.layout.fragment_coin_detail){

    private lateinit var coindetailEntity :CoinDetailEntity
    private val bundle: CoinDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<CoinDetailViewModel>()
    private val coinId:String by lazy { bundle.id }
    private val back : String by lazy { bundle.back }
    private var isFav :Boolean=false

    override fun initView(view: View) {
        binding.detail=this
        initBackPress(view)
        initLiveDataObservers()
        viewModel.getCoin(coinId)
        viewModel.getFavourite(coinId)
    }

    fun backButton(view: View){
        if(back==Constants.COLLECTION_FAV){
            Navigation.findNavController(view).navigate(R.id.action_coinDetailFragment_to_favCoinsFragment)
        }else{
            Navigation.findNavController(view).navigate(R.id.action_coinDetailFragment_to_coinListFragment)
        }

    }


    fun favClick(){
        if(!isFav){
            viewModel.addFavourite(coindetailEntity.asCoinDetail())
        }else{
            viewModel.delete(coinId)
        }

        isFav=!isFav
        viewModel._isFavourite.value=UiState.Success(isFav)
    }

    private fun initBackPress(view:View){
        activity?.onBackPressedDispatcher?.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(back==Constants.COLLECTION_FAV){
                    Navigation.findNavController(view).navigate(R.id.action_coinDetailFragment_to_favCoinsFragment)
                }else{
                    Navigation.findNavController(view).navigate(R.id.action_coinDetailFragment_to_coinListFragment)
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun initLiveDataObservers(){

        lifecycleScope.launch {
            viewModel._isFavourite.observe(viewLifecycleOwner){
                when(it){
                    is UiState.Error -> {

                        //TODO fail dialog eklenecek
                        binding.favprogress.visibility=View.GONE
                        isFav=false
                        binding.imageView2.setImageResource(R.drawable.empty_fav)
                    }
                    UiState.Loading -> {
                        binding.favprogress.visibility=View.VISIBLE
                    }
                    is UiState.Success -> {
                        isFav = it.result
                        if(it.result){
                            binding.imageView2.setImageResource(R.drawable.fav)
                        }else{
                            binding.imageView2.setImageResource(R.drawable.empty_fav)
                        }
                        binding.favprogress.visibility=View.GONE
                    }
                }
            }
        }

        lifecycleScope.launch{
            viewModel.favouriteDelete.handleState(
                onLoading = {
                    binding.favprogress.visibility=View.VISIBLE
                },
                onSucces = {
                    binding.favprogress.visibility=View.GONE
                    Toast.makeText(context,getString(R.string.delFavSuccess),Toast.LENGTH_SHORT).show()
                },
                onError = {
                    binding.favprogress.visibility=View.GONE
                    Toast.makeText(context,getString(R.string.delFavFail),Toast.LENGTH_SHORT).show()
                }
            )
        }

        lifecycleScope.launch {
            viewModel.favouriteAdd.handleState(
                onLoading = {
                    binding.favprogress.visibility=View.VISIBLE
                },
                onSucces = {
                    binding.favprogress.visibility=View.GONE
                    Toast.makeText(context,getString(R.string.addFavSuccess),Toast.LENGTH_SHORT).show()
                },
                onError = {
                    binding.favprogress.visibility=View.GONE
                    Toast.makeText(context,getString(R.string.addFavFail),Toast.LENGTH_SHORT).show()
                }
            )
        }

        lifecycleScope.launch {
            viewModel.coinDetail.handleState(
                onLoading = {
                    binding.progress.visibility=View.VISIBLE
                },
                onSucces = {
                    coindetailEntity=it
                    binding.coinName.text=it.name
                    binding.coinsymbol.text=it.symbol
                    binding.imageView.loadFromUrl(it.image.large)
                    binding.lastUpdate.text=it.lastUpdated.setDateTime(requireContext())
                    binding.priceChange24h.text=getString(R.string.priceChange24hText,it.marketData.currentPrice.usd.toString())
                    binding.arrow.setArrow(it.marketData.priceChangePercentage24h)
                    binding.precentage24hText.text= getString(R.string.precentage24hText,it.marketData.priceChangePercentage24h.toFloat())
                    binding.lowest.text=getString(R.string.h24h,it.marketData.low24h.usd.toString())
                    binding.highest.text=getString(R.string.h24h,it.marketData.high24h.usd.toString())
                    binding.description.text=it.description.en.fixedString()
                    binding.progress.visibility=View.GONE
                },
                onError = {
                    binding.progress.visibility=View.GONE
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    //TODO fail dialog eklenecek
                }
            )
        }
    }
}