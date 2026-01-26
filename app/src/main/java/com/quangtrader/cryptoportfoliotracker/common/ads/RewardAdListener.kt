package com.quangtrader.cryptoportfoliotracker.common.ads

interface RewardAdListener {
    fun onAdLoaded()
    fun onAdFailedToLoad(errorCode: Int)
    fun onUserEarnedReward(amount: Int, type: String)
    fun onAdClosed()
    fun onAdShowed()
}