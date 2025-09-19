package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class InfoToken(
    @SerializedName("id")
    val id: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("categories")
    val categories: List<String>,
    val links: Links,
    @SerializedName("market_cap_rank")
    val marketCapRank: Long,
    @SerializedName("market_data")
    val marketData: MarketData,

)