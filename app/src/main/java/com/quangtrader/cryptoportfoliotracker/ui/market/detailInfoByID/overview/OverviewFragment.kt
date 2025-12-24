package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.overview

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.compose.ui.unit.Constraints
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import authenticator.app.otp.authentication.fa.common.extentions.formatNumberWithCommas
import authenticator.app.otp.authentication.fa.common.extentions.getTradingViewChartHtml
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentOverviewBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import com.quangtrader.cryptoportfoliotracker.utils.formatMarketCap
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
            val intent = requireActivity().intent
            val name = intent.getStringExtra(Constants.EXTRA_NAME_COIN)
            if (name != null) {
                tokenName = name
            }

            val rankerMarket = intent.getIntExtra(Constants.EXTRA_MARKET_RANK_COIN, -1)
            val priceMarket = intent.getDoubleExtra(Constants.EXTRA_PRICE_COIN, 0.0)
            val symbolToken = intent.getStringExtra(Constants.EXTRA_SYMBOL_COIN)
            val logoToken = intent.getStringExtra(Constants.EXTRA_LOGO_COIN)
            nameToken.text = name
            textMarketRank.text = "#".plus(rankerMarket.toString())
            priceToken.text = "$".plus(priceMarket.formatPriceTrending(priceMarket))
            val dataPercent24H = intent.getDoubleExtra(Constants.EXTRA_PRICE_24H, 0.0)
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
                overViewModel.getAllInfoToken(it.lowercase())
                viewLifecycleOwner.lifecycleScope.launch {
                    overViewModel.infoToken.collect { token ->
                        token?.data?.values?.forEach { it ->
                            it.quote.USD?.marketCap?.let { it1 ->
                                dataMarketCap.text = formatMarketCap(
                                    it1
                                )
                            }
                            dataFullyDilutedValuation.text = formatMarketCap(
                                it.quote.USD?.fullyDilutedMarketCap ?: 0.0
                            )
                            it.quote.USD?.volume24h?.let { it1 ->
                                dataTotalVol.text = formatMarketCap(
                                    it1
                                )
                            }
                            percentChange1H.text = it.quote.USD?.percentChange1h.formatPercent()
                            percentChange24H.text = it.quote.USD?.percentChange24h.formatPercent()
                            percentChange7D.text = it.quote.USD?.percentChange7d.formatPercent()
                            percentChange30D.text = it.quote.USD?.percentChange30d.formatPercent()
                        }
                    }
                }
            }

            notificationImg.clicks {
                symbolToken?.let { it1 ->
                    logoToken?.let { logo ->
//                        moveScreenAlert(
//                            logo,
//                            it1
//                        )
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
                null,
            )
        }
    }

    private fun handleInterval(tokenSymbol: String) {
        binding.apply {
            chipGroupTimeRange.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.chip15M -> loadChart(tokenSymbol, "15")
                    R.id.chip1h -> loadChart(tokenSymbol, "60")
                    R.id.chip4h -> loadChart(tokenSymbol, "240")
                    R.id.chip1d -> loadChart(tokenSymbol, "D")
                    R.id.chip1w -> loadChart(tokenSymbol, "W")
                    R.id.chip1m -> loadChart(tokenSymbol, "M")
                }
            }

        }
    }

//    private fun moveScreenAlert(logoToken: String, symbolToken: String) {
//        val intent = Intent(requireContext(), AlertTokenActivity::class.java)
//        intent.putExtra(Constants.EXTRA_LOGO_COIN, logoToken)
//        intent.putExtra(Constants.EXTRA_SYMBOL_COIN, symbolToken)
//        startActivity(intent)
//    }

}