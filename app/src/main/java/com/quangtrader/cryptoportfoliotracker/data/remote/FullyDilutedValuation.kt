package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class FullyDilutedValuation(
    @SerializedName("usd")
    val usd: Long,

)