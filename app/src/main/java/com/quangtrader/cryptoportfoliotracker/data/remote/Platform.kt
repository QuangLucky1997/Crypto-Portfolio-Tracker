package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Platform(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("symbol") var symbol: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("token_address") var tokenAddress: String? = null
)