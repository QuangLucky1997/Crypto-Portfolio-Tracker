package com.quangtrader.cryptoportfoliotracker.common.ads

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.quangtrader.cryptoportfoliotracker.inject.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class OpenAdManager(private val key: String, private val keyBackup: String, private val interval: Long = 0, private val fullAndOpenInterval: Long = 0) {

    private var appOpen: AppOpenAd? = null
    private var isLoadingAd = false
    private var isCallingCallBack = false
    private var countFailed = 0
    private var jobTimeout: Job? = null
    var adLastShownTime: Long = 0
    var isShowingAd = false

    fun loadAd(activity: AppCompatActivity, callBack: (isSuccess: Boolean) -> Unit = {}) {
        if (appOpen != null) {
            callBack(true)
            return
        }

        if (isLoadingAd) {
            return
        }

        isLoadingAd = true
        isCallingCallBack = false
        Timber.e("Load open ad screen: ${activity.javaClass.simpleName} --- ${if (countFailed == 0) key else keyBackup} --- Count Failed: $countFailed")

        val markFailed = {
            countFailed++

            when {
                countFailed >= 2 -> {
                    if (!isCallingCallBack) {
                        jobTimeout?.cancel()
                        jobTimeout = null

                        appOpen = null
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

        fun markSuccess(ad: AppOpenAd) {
//            if (BuildConfig.DEBUG && countFailed == 0) {
//                markFailed()
//                return
//            }
            if (!isCallingCallBack) {
                jobTimeout?.cancel()
                jobTimeout = null

                appOpen = ad
                isLoadingAd = false
                isCallingCallBack = true
                callBack(true)
            }
        }

        jobTimeout = activity.lifecycleScope.launch(Dispatchers.Main) {
            delay(10000L)
            markFailed()
        }

        AppOpenAd.load(
            activity,
            if (countFailed == 0) key else keyBackup,
            AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    markSuccess(ad)

                    ad.setOnPaidEventListener { adValue ->
                        val valueMicros = adValue.valueMicros
                        val currencyCode = adValue.currencyCode
                        val extras = ad.responseInfo?.responseExtras
                        val loadedAdapterResponseInfo = ad.responseInfo?.loadedAdapterResponseInfo
                        val adSourceName = loadedAdapterResponseInfo?.adSourceName
                        val mediationABTestName = extras?.getString("mediation_ab_test_name")?.takeIf { it.isNotEmpty() } ?: "null"
                        val mediationABTestVariant = extras?.getString("mediation_ab_test_variant")?.takeIf { it.isNotEmpty() } ?: "null"
                        AnalyticManager.eventTrackingAdRevenue(valueMicros, "app_open", mediationABTestName, mediationABTestVariant, currencyCode, adSourceName)
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    markFailed()
                }
            }
        )
    }

    fun isAdAvailable(): Boolean {
        return when (App.app.lastedAdTypeShown) {
            AdType.OpenAd -> appOpen != null && System.currentTimeMillis() - adLastShownTime >= interval
            AdType.Full -> appOpen != null && System.currentTimeMillis() - App.app.lastedAdLastShownTime >= fullAndOpenInterval
            else -> appOpen != null && System.currentTimeMillis() - adLastShownTime >= interval
        }
    }

    fun isTimeAdAvailable(): Boolean {
        return when (App.app.lastedAdTypeShown) {
            AdType.OpenAd -> System.currentTimeMillis() - adLastShownTime >= interval
            AdType.Full -> System.currentTimeMillis() - App.app.lastedAdLastShownTime >= fullAndOpenInterval
            else ->  System.currentTimeMillis() - adLastShownTime >= interval
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

        appOpen?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    countFailed = 0
                    appOpen = null
                    isShowingAd = false
                    adLastShownTime = System.currentTimeMillis()
                    App.app.canOpenBackground = true
                    if (isLoadAgain) loadAd(activity)

                    task()
                }
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    countFailed = 0
                    appOpen = null
                    isShowingAd = false
                    adLastShownTime = System.currentTimeMillis()
                    App.app.canOpenBackground = true
                    if (isLoadAgain) loadAd(activity)

                    task()
                }
            }

            isShowingAd = true
            App.app.canOpenBackground = false
            App.app.lastedAdTypeShown = AdType.OpenAd
            App.app.lastedAdLastShownTime = System.currentTimeMillis()

            ad.show(activity)
        } ?: run {
            task()
        }
    }
}