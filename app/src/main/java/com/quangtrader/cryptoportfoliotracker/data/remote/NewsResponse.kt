package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NewsResponse(
    @SerializedName("id") val id: String,
    @SerializedName("searchKeyWords") val searchKeyWords: List<String>?,
    @SerializedName("feedDate") val feedDate: Long,
    @SerializedName("source") val source: String,
    @SerializedName("title") val title: String,
    @SerializedName("sourceLink") val sourceLink: String,
    @SerializedName("isFeatured") val isFeatured: Boolean,
    @SerializedName("imgUrl") val imgUrl: String?,
    @SerializedName("reactionsCount") val reactionsCount: Map<String, Int>?,
    @SerializedName("reactions") val reactions: List<String>?,
    @SerializedName("shareURL") val shareURL: String?,
    @SerializedName("relatedCoins") val relatedCoins: List<String>?,
    @SerializedName("content") val content: Boolean,
    @SerializedName("link") val link: String,
    @SerializedName("bigImg") val bigImg: Boolean,
    @SerializedName("description") val description: String?,
    @SerializedName("coins") val coins: List<Coin>?
)

data class Coin(
    @SerializedName("coinKeyWords") val coinKeyWords: String?,
    @SerializedName("coinPercent") val coinPercent: Double?,
    @SerializedName("coinTitleKeyWords") val coinTitleKeyWords: String?,
    @SerializedName("coinNameKeyWords") val coinNameKeyWords: String?,
    @SerializedName("coinIdKeyWords") val coinIdKeyWords: String?
)
