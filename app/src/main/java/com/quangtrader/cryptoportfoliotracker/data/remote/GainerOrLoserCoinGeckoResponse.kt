package com.quangtrader.cryptoportfoliotracker.data.remote

import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GainerOrLoserCoinGeckoResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("current_price") val currentPrice: Double?,
    @SerializedName("market_cap") val marketCap: Long?,
    @SerializedName("market_cap_rank") val marketCapRank: Int?,
    @SerializedName("fully_diluted_valuation") val fullyDilutedValuation: Long?,
    @SerializedName("total_volume") val totalVolume: Double??,
    @SerializedName("high_24h") val high24h: Double?,
    @SerializedName("low_24h") val low24h: Double?,
    @SerializedName("price_change_24h") val priceChange24h: Double?,
    @SerializedName("price_change_percentage_24h") val priceChangePercentage24h: Double?,
    @SerializedName("market_cap_change_24h") val marketCapChange24h: Double?,
    @SerializedName("market_cap_change_percentage_24h") val marketCapChangePercentage24h: Double?,
    @SerializedName("circulating_supply") val circulatingSupply: Double?,
    @SerializedName("total_supply") val totalSupply: Double?,
    @SerializedName("max_supply") val maxSupply: Double?,
    @SerializedName("ath") val ath: Double?,
    @SerializedName("ath_change_percentage") val athChangePercentage: Double?,
    @SerializedName("ath_date") val athDate: String?,
    @SerializedName("atl") val atl: Double?,
    @SerializedName("atl_change_percentage") val atlChangePercentage: Double?,
    @SerializedName("atl_date") val atlDate: String?,
    @SerializedName("roi") val roi: Any?, // vì có thể null hoặc object
    @SerializedName("last_updated") val lastUpdated: String?,
    @SerializedName("price_change_percentage_24h_in_currency") val priceChangePercentage24hInCurrency: Double?
)