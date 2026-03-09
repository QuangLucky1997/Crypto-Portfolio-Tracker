package com.quangtrader.cryptoportfoliotracker.ui.market.detailInfoByID.overview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.common.utils.formatPercent
import com.quangtrader.cryptoportfoliotracker.common.utils.formatPriceTrending
import com.quangtrader.cryptoportfoliotracker.common.utils.getTradingViewChartHtml
import com.quangtrader.cryptoportfoliotracker.databinding.FragmentOverviewBinding
import com.quangtrader.cryptoportfoliotracker.helper.UiState
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseFragment
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

            val priceMarket = intent.getDoubleExtra(Constants.EXTRA_PRICE_COIN, 0.0)
            val symbolToken = intent.getStringExtra(Constants.EXTRA_SYMBOL_COIN)
            val logoToken = intent.getStringExtra(Constants.EXTRA_LOGO_COIN)
            nameToken.text = name
            priceToken.text = "$".plus(priceMarket.formatPriceTrending(priceMarket))
            val dataPercent24H = intent.getDoubleExtra(Constants.EXTRA_PRICE_24H, 0.0).apply {
                if (this >= 0) {
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
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        overViewModel.uiState.collect { state ->
                            when (state) {
                                is UiState.Success -> {
                                    val data = state.data
                                    with(binding) {
                                        dataMarketCap.text = data.marketCap
                                        dataFullyDilutedValuation.text = data.fullyDilutedValuation
                                        dataTotalVol.text = data.totalVol
                                        percentChange1H.text = data.change1H
                                        percentChange24H.text = data.change24H
                                        percentChange7D.text = data.change7D
                                        percentChange30D.text = data.change30D
                                    }
                                }

                                is UiState.Loading -> {}
                                is UiState.Error -> {}
                                else -> {}
                            }
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
        val data = dataNameCoin
        binding.apply {
            candleChartView.settings.javaScriptEnabled = true
            candleChartView.webViewClient = WebViewClient()
            val htmlContent =
                getTradingViewChartHtml("CRYPTO:${dataNameCoin?.uppercase()}USD", dataTime)
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