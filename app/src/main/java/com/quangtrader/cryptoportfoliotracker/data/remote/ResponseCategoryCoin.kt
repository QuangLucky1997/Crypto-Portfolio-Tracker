package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResponseCategoryCoin(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("market_cap") val marketCap: Double?,
    @SerializedName("market_cap_change_24h") val marketCapChange24h: Double?,
    @SerializedName("content") val content: String?,
    @SerializedName("top_3_coins_id") val top3CoinsId: List<String>?,
    @SerializedName("top_3_coins") val top3Coins: List<String>?,
    @SerializedName("volume_24h") val volume24h: Double?,
    @SerializedName("updated_at") val updatedAt: String?
)