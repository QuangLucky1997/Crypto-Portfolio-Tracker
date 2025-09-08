package com.quangtrader.cryptoportfoliotracker.data.remote

import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CoinItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("coin_id")
    val coinId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("market_cap_rank")
    val marketCapRank: Int?,

    @SerializedName("thumb")
    val thumb: String,

    @SerializedName("small")
    val small: String,

    @SerializedName("large")
    val large: String,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("price_btc")
    val priceBtc: Double,

    @SerializedName("score")
    val score: Int,

    @SerializedName("data")
    val data: CoinTrending?
)