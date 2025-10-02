package com.quangtrader.cryptoportfoliotracker.ui.news

import android.webkit.WebViewClient
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityShowNewsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowNewsActivity : BaseActivity<ActivityShowNewsBinding>(ActivityShowNewsBinding::inflate) {
    override fun onCreateView() {
        super.onCreateView()
        val dataSource = intent.getStringExtra(Constants.EXTRA_SOURCE_NEWS)
        val dataTime = intent.getStringExtra(Constants.EXTRA_TIME_POST)
        val dataLink = intent.getStringExtra(Constants.EXTRA_LINK_NEWS)
        binding.apply {
            timeText.text = dataTime
            sourceText.text = dataSource
            webNews.settings
            webNews.webViewClient = WebViewClient()
            dataLink?.let { it ->
                webNews.loadUrl(it)
            }
            backButton.clicks {
                finish()
            }
        }
    }
}