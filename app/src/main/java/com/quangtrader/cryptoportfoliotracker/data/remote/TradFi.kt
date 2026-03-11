package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TradFi(
    @SerializedName("symbol") val symbol: String,

    @SerializedName("priceChange") val priceChange: String,

    @SerializedName("priceChangePercent") val priceChangePercent: String,

    @SerializedName("lastPrice") val lastPrice: String,

    @SerializedName("lastQty") val lastQty: String,

    @SerializedName("highPrice") val highPrice: String,

    @SerializedName("lowPrice") val lowPrice: String,

    @SerializedName("volume") val volume: Double,

    @SerializedName("quoteVolume") val quoteVolume: String,

    @SerializedName("openPrice") val openPrice: String,

    @SerializedName("openTime") val openTime: Long,

    @SerializedName("closeTime") val closeTime: Long,

    @SerializedName("askPrice") val askPrice: String,

    @SerializedName("askQty") val askQty: String,

    @SerializedName("bidPrice") val bidPrice: String,

    @SerializedName("bidQty") val bidQty: String
)
