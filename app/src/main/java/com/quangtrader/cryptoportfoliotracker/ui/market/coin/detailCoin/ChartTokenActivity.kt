package com.quangtrader.cryptoportfoliotracker.ui.market.coin.detailCoin

import android.annotation.SuppressLint
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.R
import com.quangtrader.cryptoportfoliotracker.data.repository.ManagerDBRoomRepository
import com.quangtrader.cryptoportfoliotracker.data.roommodel.CoinFav
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityChartTokenBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.ui.market.watchlists.WatchListsViewModel
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChartTokenActivity :
    BaseActivity<ActivityChartTokenBinding>(ActivityChartTokenBinding::inflate) {
    private val coinFavViewModel by viewModels<WatchListsViewModel>()
    var isSelect = false

    @Inject
    lateinit var coinManagerDBRoomRepository: ManagerDBRoomRepository
    override fun onCreateView() {
        super.onCreateView()
        val nameCoin = intent.getStringExtra(Constants.EXTRA_NAME_COIN)
        val logoCoin = intent.getStringExtra(Constants.EXTRA_LOGO_COIN)
        val priceCoin = intent.getDoubleExtra(Constants.EXTRA_PRICE_COIN,0.0)
        val symbolCoin = intent.getStringExtra(Constants.EXTRA_SYMBOL_COIN)
        val percent24H = intent.getDoubleExtra(Constants.EXTRA_PERCENT_24_H, 0.0)
        val logo = intent.getStringExtra(Constants.EXTRA_LOGO)
        val marketCap = intent.getDoubleExtra(Constants.EXTRA_MARKET_CAP, 0.0)
        nameCoin?.let {
            logoCoin?.let { it1 ->
                symbolCoin?.let { symbolCoin1 ->
                    setData(
                        it,
                        it1,
                        symbolCoin1
                    )
                }
            }
        }
        initHandle()
        addOrRemoveFav(
            nameCoin.toString(),
            symbolCoin.toString(),
            priceCoin,
            percent24H,
            marketCap,
            logo
        )
    }

    private fun initHandle() {
        binding.apply {
            imgBack.clicks {
                finish()
            }
        }
    }

    private fun addOrRemoveFav(
        nameData: String,
        symbolCoin: String,
        priceData: Double,
        percentChange24h: Double,
        marketCap: Double,
        logo: String?
    ) {
        binding.apply {
            imgStar.clicks {
                val newFav = !isSelect
                val coinFav = CoinFav(
                    0,
                    nameData,
                    symbolCoin,
                    priceData,
                    percentChange24h,
                    marketCap,
                    logo,
                    newFav
                )
                coinFavViewModel.toggleFav(coinFav, newFav)
            }

        }


    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun setData(nameCoin: String, logoCoin: String, symbolCoin: String) {
        lifecycleScope.launch {
            coinFavViewModel.getAllWatchListsCoin.collect { favList ->
                val isFav = favList.any { it.name == nameCoin }
                binding.imgStar.setImageResource(if (isFav) R.drawable.ic_started else R.drawable.ic_star)
            }
        }
        binding.apply {
            Glide.with(root).load(logoCoin).into(imgToken)
            tokenName.text = nameCoin
            webChart.settings.javaScriptEnabled = true
            webChart.webViewClient = WebViewClient()
            val htmlContent = getTradingViewChartHtml("BINANCE:${symbolCoin}USDT")
            webChart.loadDataWithBaseURL(
                "https://www.tradingview.com",
                 htmlContent,
                "text/html",
                "UTF-8",
                null
            )
        }
    }

    private fun getTradingViewChartHtml(symbol: String): String {
        return """
        <html>
        <head>
            <style>
                body, html { margin: 0; padding: 0; width: 100%; height: 100%; overflow: hidden; }
                .tradingview-widget-container { width: 100%; height: 100%; }
            </style>
        </head>
        <body>
            <div class="tradingview-widget-container" style="height:100%;width:100%">
                <div id="tradingview_123456789"></div>
                <script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
                <script type="text/javascript">
                    new TradingView.widget({
                        "width": "100%",
                        "height": "100%",
                        "symbol": "$symbol",
                        "interval": "D",
                        "timezone": "Etc/UTC",
                        "theme": "light",
                        "style": "1",
                        "locale": "vi_VN",
                        "toolbar_bg": "#f1f3f6",
                        "enable_publishing": false,
                        "allow_symbol_change": true,
                        "container_id": "tradingview_123456789",
                         "hide_top_toolbar": false,   // ẩn top toolbar
                "hide_side_toolbar": false,
                "allow_symbol_change": false
                    });
                </script>
            </div>
        </body>
        </html>
        """.trimIndent()
    }
}
