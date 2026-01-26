package com.quangtrader.cryptoportfoliotracker.common.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RewardAdManager @Inject constructor(
    private val context: Context
) {
    private var rewardedAd: RewardedAd? = null
    private var isLoading = false

    fun loadAd(adId: String, onAdLoaded: (() -> Unit)? = null) {
        if (rewardedAd != null || isLoading) return

        isLoading = true
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(context, adId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                rewardedAd = null
                isLoading = false
            }

            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
                isLoading = false
                onAdLoaded?.invoke()
            }
        })
    }


    fun showAd(activity: Activity, listener: RewardAdListener) {
        val ad = rewardedAd
        if (ad == null) {
            listener.onAdFailedToLoad(-1)
            return
        }

        ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                listener.onAdClosed()
            }

            override fun onAdShowedFullScreenContent() {
                listener.onAdShowed()
            }
        }

        ad.show(activity) { rewardItem ->
            listener.onUserEarnedReward(rewardItem.amount, rewardItem.type)
        }
    }

    fun isAdReady(): Boolean = rewardedAd != null
}