package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class Data(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("symbol") var symbol: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("num_market_pairs") var numMarketPairs: Int? = null,
    @SerializedName("date_added") var dateAdded: String? = null,
    @SerializedName("tags") var tags: ArrayList<String> = arrayListOf(),
    @SerializedName("max_supply") var maxSupply: Double? = null,
    @SerializedName("circulating_supply") var circulatingSupply: Double? = null,
    @SerializedName("total_supply") var totalSupply: Double? = null,
    @SerializedName("infinite_supply") var infiniteSupply: Boolean? = null,
    @SerializedName("platform") var platform: Platform? = Platform(),
    @SerializedName("cmc_rank") var cmcRank: Int? = null,
    @SerializedName("self_reported_circulating_supply") var selfReportedCirculatingSupply: String? = null,
    @SerializedName("self_reported_market_cap") var selfReportedMarketCap: String? = null,
    @SerializedName("tvl_ratio") var tvlRatio: String? = null,
    @SerializedName("last_updated") var lastUpdated: String? = null,
    @SerializedName("quote") var quote: Quote? = Quote()

)