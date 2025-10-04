package com.quangtrader.cryptoportfoliotracker.ui.news

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import authenticator.app.otp.authentication.fa.common.extentions.clicks
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityShowNewsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            webNews.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    showLoading(true)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    showLoading(false)
                }
            }
            dataLink?.let { it ->
                webNews.loadUrl(it)
            }
            backButton.clicks {
                finish()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.apply {
            if (show) {
                animationLoading.visibility = View.VISIBLE
                webNews.visibility = View.INVISIBLE
                animationLoading.playAnimation()
            } else {
                lifecycleScope.launch {
                    delay(800) // ví dụ 0.8s
                    animationLoading.cancelAnimation()
                    animationLoading.visibility = View.GONE
                    webNews.visibility = View.VISIBLE
                }
            }
        }

    }

}