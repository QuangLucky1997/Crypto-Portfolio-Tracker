package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class CoinData(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("logo") val logo: String?,
    @SerializedName("subreddit") val subreddit: String?,
    @SerializedName("notice") val notice: String?,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("tag-names") val tagNames: List<String>?,
    @SerializedName("tag-groups") val tagGroups: List<String>?,
    @SerializedName("urls") val urls: Urls?,
    @SerializedName("platform") val platform: Platform?,
    @SerializedName("date_added") val dateAdded: String?,
    @SerializedName("twitter_username") val twitterUsername: String?,
    @SerializedName("is_hidden") val isHidden: Int?,
    @SerializedName("date_launched") val dateLaunched: String?,
    @SerializedName("contract_address") val contractAddress: List<ContractAddress>?,
    @SerializedName("self_reported_circulating_supply") val selfReportedCirculatingSupply: Double?,
    @SerializedName("self_reported_tags") val selfReportedTags: List<String>?,
    @SerializedName("self_reported_market_cap") val selfReportedMarketCap: Double?,
    @SerializedName("infinite_supply") val infiniteSupply: Boolean?
)