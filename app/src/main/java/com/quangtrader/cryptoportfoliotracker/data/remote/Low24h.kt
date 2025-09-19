package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class Low24h(
    @SerializedName("usd")
    val usd: Double,
)