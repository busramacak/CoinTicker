package com.bmprj.cointicker.ui.detail


import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentCoinDetailBinding
import com.bmprj.cointicker.domain.coinList.CoinDetailEntity
import com.bmprj.cointicker.domain.coinList.asCoinDetail
import com.bmprj.cointicker.utils.Constants
import com.bmprj.cointicker.utils.fixedString
import com.bmprj.cointicker.utils.loadFromUrl
import com.bmprj.cointicker.utils.setArrow
import com.bmprj.cointicker.utils.setDateTime
import com.bmprj.cointicker.utils.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CoinDetailFragment : BaseFragment<FragmentCoinDetailBinding>(R.layout.fragment_coin_detail) {

    private lateinit var coinDetailEntity: CoinDetailEntity // todo initialize edildi mi edilmedi mi? 54.satır
    private val bundle: CoinDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<CoinDetailViewModel>()
    private val coinId: String by lazy { bundle.id }
    private val back: String by lazy { bundle.back }
    private var isFav: Boolean = false
    private val findNavController by lazy { findNavController() }

    override fun initView(view: View) {
        binding.detail = this
        initBackPress()
        initLiveDataObservers()
        viewModel.getCoin(coinId)
        viewModel.getFavourite(coinId)
    }

    fun backButton() {
        val action = if (back == Constants.COLLECTION_FAV) {
            CoinDetailFragmentDirections.actionCoinDetailFragmentToFavCoinsFragment()
        } else {
            CoinDetailFragmentDirections.actionCoinDetailFragmentToCoinListFragment()
        }

        findNavController.navigate(action)
    }

    fun favClick() {
       //todo https://blog.jetbrains.com/kotlin/2017/09/kotlin-1-2-beta-is-out/#lateinit-improvements
        if (!isFav) { //todo:: kullanarak classı referans alıyor. sınıfın üyelerine erişiyor. :: koymayınca kontrol edemedim.
            if(this::coinDetailEntity.isInitialized)
                viewModel.addFavourite(coinDetailEntity.asCoinDetail())
        } else {
            viewModel.delete(coinId)
        }

        isFav = !isFav
        viewModel.setFavouriteState(isFav)
    }

    private fun initBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = if (back == Constants.COLLECTION_FAV) {
                    CoinDetailFragmentDirections.actionCoinDetailFragmentToFavCoinsFragment()
                } else {
                    CoinDetailFragmentDirections.actionCoinDetailFragmentToCoinListFragment()
                }
                findNavController.navigate(action)
            }
        })
    }

    private fun initLiveDataObservers() {

        with(binding) {
            viewModel.isFavourite.handleState(onLoading = {
                favProgress.visibility = View.VISIBLE
            }, onError = {
                favProgress.visibility = View.GONE
                isFav = false
                imageView2.setImageResource(R.drawable.empty_fav)
            }, onSucces = { result ->

                isFav = result
// todo (any other way better than this?) try to refactor
                imageView2.setImageResource(
                    if (result) { R.drawable.fav }
                    else { R.drawable.empty_fav })

                favProgress.visibility = View.GONE
            })

            viewModel.favouriteDelete.handleState(onLoading = {
                favProgress.visibility = View.VISIBLE
            }, onSucces = {
                favProgress.visibility = View.GONE
                toast(R.string.delFavSuccess)
            }, onError = {
                favProgress.visibility = View.GONE
                toast(it.message)
            })

            viewModel.favouriteAdd.handleState(onLoading = {
                favProgress.visibility = View.VISIBLE
            }, onSucces = {
                favProgress.visibility = View.GONE
                toast(R.string.addFavSuccess)
            }, onError = {
                favProgress.visibility = View.GONE
                toast(it.message)
            })

            viewModel.coinDetail.handleState(onLoading = {
                progresBar.visibility = View.VISIBLE
            }, onSucces = {

                coinDetailEntity = it
                coinName.text = it.name
                coinSymbol.text = it.symbol
                coinImageView.loadFromUrl(it.image.large)
                lastUpdate.text = it.lastUpdated.setDateTime(requireContext())
                priceChange24h.text = getString(
                    R.string.priceChange24hText, it.marketData.currentPrice.usd.toString()
                )
                arrow.setArrow(it.marketData.priceChangePercentage24h)
                precentage24hText.text = getString(
                    R.string.precentage24hText, it.marketData.priceChangePercentage24h.toFloat()
                )
                lowest.text = getString(R.string.h24h, it.marketData.low24h.usd.toString())
                highest.text = getString(R.string.h24h, it.marketData.high24h.usd.toString())
                description.text = it.description.en.fixedString()
                progresBar.visibility = View.GONE
            }, onError = {
                progresBar.visibility = View.GONE
                toast(it.message)
            })
        }
    }
}