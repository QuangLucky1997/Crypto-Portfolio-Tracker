package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class PlaformCoin(
    @SerializedName("name") val name: String?,
    @SerializedName("coin") val coin: CoinDetail?
)
