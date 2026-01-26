package com.quangtrader.cryptoportfoliotracker.common.ads

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.quangtrader.cryptoportfoliotracker.inject.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class FullAdManager(private val key: String, private val keyBackup: String, private val fullDisplayInterval: Long = 0, private val fullAndOpenDisplayInterval: Long = 0) {

    private var full: InterstitialAd? = null
    private var isLoadingAd = false
    private var isCallingCallBack = false
    private var countFailed = 0
    private var jobTimeout: Job? = null
    var adLastShownTime: Long = 0
    var isShowingAd = false

    fun loadAd(activity: AppCompatActivity, callBack: (isSuccess: Boolean) -> Unit = {}) {
        if (full != null) {
            callBack(true)
            return
        }

        if (isLoadingAd) {
            return
        }

        isLoadingAd = true
        isCallingCallBack = false
        Timber.Forest.e("Load full ad screen: ${activity.javaClass.simpleName} --- ${if (countFailed == 0) key else keyBackup} --- Count Failed: $countFailed")

        val markFailed = {
            countFailed++

            when {
                countFailed >= 2 -> {
                    if (!isCallingCallBack) {
                        jobTimeout?.cancel()
                        jobTimeout = null

                        full = null
                        isLoadingAd = false
                        isCallingCallBack = true
                        callBack(false)
                    }
                }
                else -> {
                    jobTimeout?.cancel()
                    jobTimeout = null

                    isLoadingAd = false
                    loadAd(activity, callBack)
                }
            }
        }

        fun markSuccess(ad: InterstitialAd) {
//            if (BuildConfig.DEBUG && countFailed == 0) {
//                markFailed()
//                return
//            }
            if (!isCallingCallBack) {
                jobTimeout?.cancel()
                jobTimeout = null

                full = ad
                isLoadingAd = false
                isCallingCallBack = true
                callBack(true)
            }
        }

        jobTimeout = activity.lifecycleScope.launch(Dispatchers.Main) {
            delay(10000L)
            markFailed()
        }

        InterstitialAd.load(
            activity,
            if (countFailed == 0) key else keyBackup,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    markSuccess(ad)

                    ad.setOnPaidEventListener { adValue ->
                        val valueMicros = adValue.valueMicros
                        val currencyCode = adValue.currencyCode
                        val extras = ad.responseInfo?.responseExtras
                        val loadedAdapterResponseInfo = ad.responseInfo?.loadedAdapterResponseInfo
                        val adSourceName = loadedAdapterResponseInfo?.adSourceName
                        val mediationABTestName = extras?.getString("mediation_ab_test_name")?.takeIf { it.isNotEmpty() } ?: "null"
                        val mediationABTestVariant = extras?.getString("mediation_ab_test_variant")?.takeIf { it.isNotEmpty() } ?: "null"
                        AnalyticManager.eventTrackingAdRevenue(
                            valueMicros,
                            "inter",
                            mediationABTestName,
                            mediationABTestVariant,
                            currencyCode,
                            adSourceName
                        )
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    markFailed()
                }
            }
        )
    }

    fun isAdAvailable(): Boolean {
        return when (App.Companion.app.lastedAdTypeShown) {
            AdType.Full -> full != null && System.currentTimeMillis() - adLastShownTime >= fullDisplayInterval
            AdType.OpenAd -> full != null && System.currentTimeMillis() - App.Companion.app.lastedAdLastShownTime >= fullAndOpenDisplayInterval
            else -> full != null && System.currentTimeMillis() - adLastShownTime >= fullDisplayInterval
        }
    }

    fun isTimeAdAvailable(): Boolean {
        return when (App.Companion.app.lastedAdTypeShown) {
            AdType.Full -> System.currentTimeMillis() - adLastShownTime >= fullDisplayInterval
            AdType.OpenAd, AdType.Reward -> System.currentTimeMillis() - App.Companion.app.lastedAdLastShownTime >= fullAndOpenDisplayInterval
            else ->  System.currentTimeMillis() - adLastShownTime >= fullDisplayInterval
        }
    }

    fun loadOrShowIfAvailable(activity: AppCompatActivity, isLoadAgain: Boolean, task: () -> Unit = {}){
        when {
            isAdAvailable() -> showAdIfAvailable(activity, isLoadAgain, task)
            else -> loadAd(activity) { isSuccess ->
                when {
                    isSuccess -> showAdIfAvailable(activity, isLoadAgain, task)
                    else -> task()
                }
            }
        }
    }

    private fun showAdIfAvailable(activity: AppCompatActivity, isLoadAgain: Boolean, task: () -> Unit = {}) {
        if (isShowingAd) {
            return
        }

        full?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    countFailed = 0
                    full = null
                    isShowingAd = false
                    adLastShownTime = System.currentTimeMillis()
                    App.Companion.app.canOpenBackground = true
                    if (isLoadAgain) loadAd(activity)

                    task()
                }
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    countFailed = 0
                    full = null
                    isShowingAd = false
                    adLastShownTime = System.currentTimeMillis()
                    App.Companion.app.canOpenBackground = true
                    if (isLoadAgain) loadAd(activity)

                    task()
                }
            }

            isShowingAd = true
            App.Companion.app.canOpenBackground = false
            App.Companion.app.lastedAdTypeShown = AdType.Full
            App.Companion.app.lastedAdLastShownTime = System.currentTimeMillis()

            ad.show(activity)
        } ?: run {
            task()
        }
    }
}