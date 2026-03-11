package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TradFiFilterByDisplayName(
    @SerializedName("contractId") val contractId: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("size") val size: String,

    @SerializedName("quantityPrecision") val quantityPrecision: Long,

    @SerializedName("pricePrecision") val pricePrecision: Long,

    @SerializedName("feeRate") val feeRate: Double,

    @SerializedName("makerFeeRate") val makerFeeRate: Double,

    @SerializedName("takerFeeRate") val takerFeeRate: Double,

    @SerializedName("tradeMinLimit") val tradeMinLimit: Long,

    @SerializedName("tradeMinQuantity") val tradeMinQuantity: Double,

    @SerializedName("tradeMinUsdt") val tradeMinUsdt: Long,

    @SerializedName("currency") val currency: String,

    @SerializedName("asset") val asset: String,

    @SerializedName("status") val status: Long,

    @SerializedName("apiStateOpen") val apiStateOpen: String,
    @SerializedName("apiStateClose") val apiStateClose: String,
    @SerializedName("ensureTrigger") val ensureTrigger: Boolean,
    @SerializedName("triggerFeeRate") val triggerFeeRate: String,
    @SerializedName("brokerState") val brokerState: Boolean,
    @SerializedName("launchTime") val launchTime: Long,
    @SerializedName("maintainTime") val maintainTime: Long,
    @SerializedName("offTime") val offTime: Long,
    @SerializedName("displayName") val displayName: String,
)