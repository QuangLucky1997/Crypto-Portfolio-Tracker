package com.quangtrader.cryptoportfoliotracker.data.remote

import android.support.annotation.Keep

@Keep
data class CoinUI(
    val id: Int,
    val name: String,
    val symbol: String,
    val price: Double?,
    val percentChange24h: Double?,
    val marketCap: Double?,
    val logo: String? = null
)