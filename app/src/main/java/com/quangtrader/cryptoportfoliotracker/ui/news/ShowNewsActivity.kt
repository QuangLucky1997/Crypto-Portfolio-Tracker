package com.quangtrader.cryptoportfoliotracker.ui.news

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.quangtrader.cryptoportfoliotracker.common.utils.clicks
import com.quangtrader.cryptoportfoliotracker.common.utils.tryOrNull
import com.quangtrader.cryptoportfoliotracker.databinding.ActivityShowNewsBinding
import com.quangtrader.cryptoportfoliotracker.ui.base.BaseActivity
import com.quangtrader.cryptoportfoliotracker.common.utils.Constants
import com.quangtrader.cryptoportfoliotracker.common.utils.isNetworkAvailable
import com.quangtrader.cryptoportfoliotracker.helper.Preferences
import com.quangtrader.cryptoportfoliotracker.inject.App.Companion.app
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShowNewsActivity : BaseActivity<ActivityShowNewsBinding>(ActivityShowNewsBinding::inflate) {
    @Inject lateinit var preferences : Preferences
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

    private fun loadBannerAdmob() {
        tryOrNull {
            binding.viewAdNewsShow.isVisible = true
            binding.viewBanner.isVisible = true
            binding.holderLoading.root.isVisible = true
            binding.holderLoading.mShimmerFrameLayout.startShimmer()
            app.bannerDetailShowNew.loadAdMulti(this, binding.viewGroupAd) { isSuccess ->
                when {
                    isSuccess -> {
                        binding.holderLoading.root.isVisible = false
                        binding.holderLoading.mShimmerFrameLayout.stopShimmer()
                    }

                    else -> {
                        binding.viewAdNewsShow.isVisible = false
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        when {
            !preferences.isUpgraded.get() && isNetworkAvailable() && app.isBannerHome && !app.isMaxClickAdsTotal() -> {
                loadBannerAdmob()
            }

            else -> binding.viewAdNewsShow.isVisible = false
        }
    }

}