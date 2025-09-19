package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.overview

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import authenticator.app.otp.authentication.fa.common.extentions.formatNumberWithCommas
import authenticator.app.otp.authentication.fa.common.extentions.getTradingViewChartHtml
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentOverviewBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.DetailTokenActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import com.quangtrader.cryptoportfoliotracker.utils.formatPercent
import com.quangtrader.cryptoportfoliotracker.utils.formatPriceTrending
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OverviewFragment : BaseFragment<FragmentOverviewBinding>() {
    override val _binding: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOverviewBinding
        get() = FragmentOverviewBinding::inflate


    private val overViewModel by viewModels<OverviewViewModel>()
    private var tokenName = ""
    override fun onViewCreated() {
        setData()
    }

    @SuppressLint("DefaultLocale", "SetJavaScriptEnabled")
    private fun setData() {
        binding.apply {
            val activity = requireActivity() as DetailTokenActivity
            val name = activity.intent.getStringExtra(Constants.EXTRA_NAME_COIN)
            if (name != null) {
                tokenName = name
            }
            val rankerMarket = activity.intent.getIntExtra(Constants.EXTRA_MARKET_RANK_COIN, -1)
            val priceMarket = activity.intent.getDoubleExtra(Constants.EXTRA_PRICE_COIN, 0.0)
            val symbolToken = activity.intent.getStringExtra(Constants.EXTRA_SYMBOL_COIN)
            nameToken.text = name
            textMarketRank.text = "#".plus(rankerMarket.toString())
            priceToken.text = "$".plus(priceMarket.formatPriceTrending(priceMarket))
            val dataPercent24H = activity.intent.getDoubleExtra(Constants.EXTRA_PRICE_24H, 0.0)
            val percentText = String.format("%.2f%%", dataPercent24H)
            val colorRes = dataPercent24H.let {
                if (it >= 0) {
                    imgUpDown.setImageResource(R.drawable.up_trend)
                } else {
                    imgUpDown.setImageResource(R.drawable.down_trend)
                }
            }
            percentRealtime.text = dataPercent24H.formatPercent()
            symbolToken?.let {
                loadChart(it, "D")
                handleInterval(it)
            }
            overViewModel.getAllInfoToken(tokenName.lowercase())
            viewLifecycleOwner.lifecycleScope.launch {
                overViewModel.infoToken.collect { token ->
                    if (token != null) {
                        dataMarketCap.text = "$".plus(formatNumberWithCommas( token.marketData.marketCap.usd))
                        dataFullyDilutedValuation.text =
                            "$".plus(formatNumberWithCommas(token.marketData.fullyDilutedValuation.usd))
                        dataTotalVol.text =
                            "$".plus(formatNumberWithCommas(token.marketData.totalVolume.usd))
                        dataHigh24h.text = "$".plus(token.marketData.high24h.usd.toString())
                        dataLow24h.text = "$".plus(token.marketData.low24h.usd.toString())

                        val listCategory = token.categories.forEach {
                            Log.d("Main123",it)
                        }
                    }
                }
            }


        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadChart(dataNameCoin: String, dataTime: String) {
        binding.apply {
            candleChartView.settings.javaScriptEnabled = true
            candleChartView.webViewClient = WebViewClient()
            val htmlContent =
                getTradingViewChartHtml("BINANCE:${dataNameCoin?.uppercase()}USDT", dataTime)
            candleChartView.loadDataWithBaseURL(
                "https://www.tradingview.com",
                htmlContent,
                "text/html",
                "UTF-8",
                null
            )
        }
    }

    private fun handleInterval(tokenSymbol: String) {
        binding.apply {
            chipGroupTimeRange.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.chip1h -> loadChart(tokenSymbol, "60")
                    R.id.chip1d -> loadChart(tokenSymbol, "D")
                    R.id.chip1w -> loadChart(tokenSymbol, "W")
                    R.id.chip1m -> loadChart(tokenSymbol, "M")
                }
            }
        }
    }

}