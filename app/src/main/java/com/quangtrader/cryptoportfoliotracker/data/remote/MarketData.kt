package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class MarketData(
    @SerializedName("market_cap")
    val marketCap: MarketCap,
    @SerializedName("fully_diluted_valuation")
    val fullyDilutedValuation: FullyDilutedValuation,
    @SerializedName("market_cap_fdv_ratio")
    val marketCapFdvRatio: Double,
    @SerializedName("total_volume")
    val totalVolume: TotalVolume,
    @SerializedName("high_24h")
    val high24h: High24h,
    @SerializedName("low_24h")
    val low24h: Low24h,
    @SerializedName("max_supply")
    val maxSupply: Long,
)