package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CoinTrending (
    @SerializedName("price")
    val price: Double?,

    @SerializedName("price_btc")
    val priceBtc: String?,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Map<String, Double>?,

    @SerializedName("market_cap")
    val marketCap: String?,

    @SerializedName("market_cap_btc")
    val marketCapBtc: String?,

    @SerializedName("total_volume")
    val totalVolume: String?,

    @SerializedName("total_volume_btc")
    val totalVolumeBtc: String?,

    @SerializedName("sparkline")
    val sparkline: String?,

    @SerializedName("content")
    val content: Any?
)