package com.quangtrader.cryptoportfoliotracker.data.remote

import com.google.gson.annotations.SerializedName

data class GlobalResponse(
    @SerializedName("data") val data: GlobalData
)

data class GlobalData(
    @SerializedName("active_cryptocurrencies") val activeCryptocurrencies: Int,
    @SerializedName("upcoming_icos") val upcomingIcos: Int,
    @SerializedName("ongoing_icos") val ongoingIcos: Int,
    @SerializedName("ended_icos") val endedIcos: Int,
    @SerializedName("markets") val markets: Int,
    @SerializedName("total_market_cap") val totalMarketCap: Map<String, Double>,
    @SerializedName("total_volume") val totalVolume: Map<String, Double>,
    @SerializedName("market_cap_percentage") val marketCapPercentage: Map<String, Double>,
    @SerializedName("market_cap_change_percentage_24h_usd") val marketCapChange24hUsd: Double,
    @SerializedName("updated_at") val updatedAt: Long
)
