package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TradFiResponse (
    @SerializedName("code")
    val code: Long,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("data")
    val data: List<TradFi>
)