package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class StatusCoin(
    @SerializedName("timestamp") val timestamp: String?,
    @SerializedName("error_code") val errorCode: Int?,
    @SerializedName("error_message") val errorMessage: String?,
    @SerializedName("elapsed") val elapsed: Int?,
    @SerializedName("credit_count") val creditCount: Int?,
    @SerializedName("notice") val notice: String?
)