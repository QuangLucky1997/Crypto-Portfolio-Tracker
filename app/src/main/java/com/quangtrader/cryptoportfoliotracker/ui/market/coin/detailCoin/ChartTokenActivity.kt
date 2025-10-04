package com.quangtrader.cryptoportfoliotracker.ui.market.coin.detailCoin

import android.annotation.SuppressLint
import android.webkit.WebViewClient
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.bumptech.glide.Glide
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityChartTokenBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChartTokenActivity :
    BaseActivity<ActivityChartTokenBinding>(ActivityChartTokenBinding::inflate) {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView() {
        super.onCreateView()
        setData()
        initHandle()
    }

    private fun initHandle() {
        binding.apply {
            imgBack.clicks {
                finish()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setData() {
        val nameCoin = intent.getStringExtra(Constants.EXTRA_NAME_COIN)
        val logoCoin = intent.getStringExtra(Constants.EXTRA_LOGO_COIN)
        val symbolCoin = intent.getStringExtra(Constants.EXTRA_SYMBOL_COIN)
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
