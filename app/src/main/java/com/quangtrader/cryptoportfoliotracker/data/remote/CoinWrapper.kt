package com.quangtrader.cryptoportfoliotracker.data.remote

import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CoinWrapper(
    @SerializedName("item")
    val item: CoinItem
)