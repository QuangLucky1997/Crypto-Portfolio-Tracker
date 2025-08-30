package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class Status (
    @SerializedName("timestamp"     ) var timestamp    : String? = null,
    @SerializedName("error_code"    ) var errorCode    : Int?    = null,
    @SerializedName("error_message" ) var errorMessage : String? = null,
    @SerializedName("elapsed"       ) var elapsed      : Int?    = null,
    @SerializedName("credit_count"  ) var creditCount  : Int?    = null,
    @SerializedName("notice"        ) var notice       : String? = null,
    @SerializedName("total_count"   ) var totalCount   : Int?    = null

)