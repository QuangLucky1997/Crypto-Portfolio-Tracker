package com.quangtrader.cryptoportfoliotracker.common.ads

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.quangtrader.cryptoportfoliotracker.common.utils.tryOrNull
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import timber.log.Timber

class BannerAdManager(val key: String, val isSizeLarge: Boolean = false) {

    private var isLoadingBannerAd = false
    private var isClosedBannerAd = true
    private var adView: AdView? = null

    fun loadAdMulti(
        activity: Activity,
        viewGroup: ViewGroup,
        collapse: String? = null,
        callback: (isSuccess: Boolean) -> Unit = {}
    ) {
        loadAndShowBanner(activity, viewGroup, collapse) { isSuccess ->
            when {
                isSuccess -> callback(true)
                else -> loadAndShowBanner(activity, viewGroup, collapse) { isSecondSuccess ->
                    when {
                        isSecondSuccess -> callback(true)
                        else -> callback(false)
                    }
                }
            }
        }
    }

    private fun loadAndShowBanner(
        activity: Activity,
        viewGroup: ViewGroup,
        collapse: String? = null,
        callback: (isSuccess: Boolean) -> Unit = {}
    ) {
        tryOrNull {
            if (isLoadingBannerAd || !isClosedBannerAd) {
                return@tryOrNull
            }

            Timber.e("Load banner")

            isLoadingBannerAd = true
            isClosedBannerAd = true

            val bundle = Bundle().apply {
                when {
                    collapse != null -> this.putString("collapsible", collapse)
                }
            }

            tryOrNull { viewGroup.removeAllViews() }
            tryOrNull { viewGroup.isVisible = true }

            val displayMetrics = DisplayMetrics().apply {
                activity.windowManager.defaultDisplay.getMetrics(this)
            }
            val targetAdWidth = (displayMetrics.widthPixels / displayMetrics.density).toInt()
            val targetAdHeight = 80
            val size =
                if (isSizeLarge) AdSize.MEDIUM_RECTANGLE else AdSize(targetAdWidth, targetAdHeight)

            AdView(activity).let { adView ->
                adView.setAdSize(size)
                adView.adUnitId = key
                adView.adListener = object : AdListener() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        isLoadingBannerAd = false
                        isClosedBannerAd = true
                        callback(false)
                    }

                    override fun onAdLoaded() {
                        isLoadingBannerAd = false
                        isClosedBannerAd = true
                        callback(true)
                    }

                    override fun onAdClosed() {
                        isClosedBannerAd = true
                    }

                    override fun onAdOpened() {
                        isClosedBannerAd = false
                    }
                }
                this.adView = adView
                this.adView?.let { ad ->
                    ad.setOnPaidEventListener { adValue ->
                        // Extract the impression-level ad revenue data.
                        val valueMicros = adValue.valueMicros
                        val currencyCode = adValue.currencyCode
                        val extras = ad.responseInfo?.responseExtras

                        val loadedAdapterResponseInfo = ad.responseInfo?.loadedAdapterResponseInfo
                        val adSourceName = loadedAdapterResponseInfo?.adSourceName

                        val mediationABTestName =
                            extras?.getString("mediation_ab_test_name")?.takeIf { it.isNotEmpty() }
                                ?: "null"
                        val mediationABTestVariant = extras?.getString("mediation_ab_test_variant")
                            ?.takeIf { it.isNotEmpty() } ?: "null"

                        AnalyticManager.eventTrackingAdRevenue(
                            valueMicros,
                            "banner",
                            mediationABTestName,
                            mediationABTestVariant,
                            currencyCode,
                            adSourceName
                        )
                    }
                }
                viewGroup.addView(adView)
                adView.loadAd(
                    AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter::class.java, bundle)
                        .build()
                )
            }
        }
    }
}
