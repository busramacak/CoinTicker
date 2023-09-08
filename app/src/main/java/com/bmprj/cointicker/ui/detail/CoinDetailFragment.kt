package com.bmprj.cointicker.ui.detail


import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.bmprj.cointicker.R
import com.bmprj.cointicker.base.BaseFragment
import com.bmprj.cointicker.databinding.FragmentCoinDetailBinding
import com.bmprj.cointicker.domain.coins.CoinDetailEntity
import com.bmprj.cointicker.domain.coins.asCoinDetail
import com.bmprj.cointicker.ui.coin.CoinListFragmentDirections
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.utils.UiState
import com.bmprj.cointicker.utils.loadFromUrl
import com.bmprj.cointicker.utils.setArrow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class CoinDetailFragment : BaseFragment<FragmentCoinDetailBinding>(R.layout.fragment_coin_detail){

    private lateinit var coindetail :CoinDetailEntity
    private val bundle: CoinDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<CoinDetailViewModel>()
    private val coinId:String by lazy { bundle.id }
    private var isFav :Boolean=false

    override fun initView(view: View) {
        binding.detail=this
        initBackPress(view)
        initLiveDataObservers()
        viewModel.getCoin(coinId)
        viewModel.getFavourite(coinId)
    }

    fun backButton(view: View){
        Navigation.findNavController(view).navigateUp()
    }

    fun favClick(){
        if(!isFav){
            viewModel.addFavourite(coindetail.asCoinDetail())
        }else{
            viewModel.delete(coinId)
        }

        isFav=!isFav
        viewModel._isFavourite.value=UiState.Success(isFav)
    }

    private fun initBackPress(view:View){
        activity?.onBackPressedDispatcher?.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.findNavController(view).navigateUp()
            }
        })
    }

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
                    coindetail=it
                    binding.coinName.text=it.name
                    binding.coinsymbol.text=it.symbol
                    binding.imageView.loadFromUrl(it.image.large)
                    val date = it.lastUpdated

                    val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    val dat: Date = inFormat.parse(date) as Date
                    val outFormatDays = SimpleDateFormat("yyyy")
                    val goal: String = outFormatDays.format(dat)
                    val outFormatMonth = SimpleDateFormat("MM")
                    val month:String = outFormatMonth.format(dat)
                    val outFormatDay = SimpleDateFormat("dd")
                    val dy : String = outFormatDay.format(dat)

                    val outFormatHour = SimpleDateFormat("HH")
                    val hour: String = outFormatHour.format(dat)
                    val outFormatMinute = SimpleDateFormat("mm")
                    val minute: String = outFormatMinute.format(dat)
                    val outFormatSecond = SimpleDateFormat("ss")
                    val second: String = outFormatSecond.format(dat)

                    binding.lastUpdate.text=getString(R.string.lastupdate,goal,month,dy,hour,minute,second)
                    binding.priceChange24h.text=getString(R.string.priceChange24hText,it.marketData.currentPrice.usd.toString())
                    binding.arrow.setArrow(it.marketData.priceChangePercentage24h)
                    binding.precentage24hText.text= getString(R.string.precentage24hText,it.marketData.priceChangePercentage24h.toFloat())
                    binding.lowest.text=getString(R.string.h24h,it.marketData.low24h.usd.toString())
                    binding.highest.text=getString(R.string.h24h,it.marketData.high24h.usd.toString())

                    val data = it.description.en
                    val hrefPattern = """<a href="([^"]*)">([^<]*)</a>""".toRegex()
                    val matcher = hrefPattern.findAll(data)
                    val duzeltilmisVeri = StringBuilder()
                    var sonIndex = 0

                    matcher.forEach { match ->
                        duzeltilmisVeri.append(data.substring(sonIndex, match.range.first)) // Önceki metin
                        val metin = match.groupValues[2] // Bağlantı metni
                        duzeltilmisVeri.append(metin) // Düzeltilmiş metni ekle
                        sonIndex = match.range.last + 1
                    }
                    duzeltilmisVeri.append(data.substring(sonIndex)) // Geri kalan metin

                    binding.description.text=duzeltilmisVeri.toString()
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