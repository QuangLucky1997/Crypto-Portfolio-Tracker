package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TickerResponse(
    @SerializedName("tickers")
    val tickers: List<Ticker>? = null
)

data class Ticker(
    @SerializedName("base")
    val base: String? = null,
    @SerializedName("target")
    val target: String? = null,
    @SerializedName("market")
    val market: Market? = null,
    @SerializedName("last")
    val last: Double? = null,
    @SerializedName("volume")
    val volume: Double? = null
)

data class Market(
    @SerializedName("name")
    val name: String? = null
)