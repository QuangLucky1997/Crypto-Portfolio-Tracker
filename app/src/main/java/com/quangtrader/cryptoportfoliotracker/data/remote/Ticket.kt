package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Ticket(
    @SerializedName("market") val market: Market,
    @SerializedName("last") val last: Double,
    @SerializedName("volume") val volume: Double,
)