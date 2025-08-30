package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class USD(
    @SerializedName("price") var price: Double? = null,
    @SerializedName("volume_24h") var volume24h: Double? = null,
    @SerializedName("volume_change_24h") var volumeChange24h: Double? = null,
    @SerializedName("percent_change_1h") var percentChange1h: Double? = null,
    @SerializedName("percent_change_24h") var percentChange24h: Double? = null,
    @SerializedName("percent_change_7d") var percentChange7d: Double? = null,
    @SerializedName("percent_change_30d") var percentChange30d: Double? = null,
    @SerializedName("percent_change_60d") var percentChange60d: Double? = null,
    @SerializedName("percent_change_90d") var percentChange90d: Double? = null,
    @SerializedName("market_cap") var marketCap: Double? = null,
    @SerializedName("market_cap_dominance") var marketCapDominance: Double? = null,
    @SerializedName("fully_diluted_market_cap") var fullyDilutedMarketCap: Double? = null,
    @SerializedName("tvl") var tvl: String? = null,
    @SerializedName("last_updated") var lastUpdated: String? = null

)