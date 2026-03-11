package com.quangtrader.cryptoportfoliotracker.data.remote

import com.google.gson.annotations.SerializedName

data class TradFiFilterResponse(
    @SerializedName("code")
    val code: Long,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("data")
    val data: List<TradFiFilterByDisplayName>
)