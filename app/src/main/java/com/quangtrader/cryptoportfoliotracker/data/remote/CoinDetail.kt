package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class CoinDetail(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("slug") val slug: String?
)