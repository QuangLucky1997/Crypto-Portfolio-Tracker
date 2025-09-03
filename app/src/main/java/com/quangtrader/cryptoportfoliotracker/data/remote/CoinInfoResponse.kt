package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class CoinInfoResponse(
    @SerializedName("status") val status: Status?,
    @SerializedName("data") val data: Map<String, CoinData>?
)