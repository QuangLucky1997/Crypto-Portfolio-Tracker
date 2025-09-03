package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ContractAddress(
    @SerializedName("contract_address") val contractAddress: String?,
    @SerializedName("platform") val platformCoin: PlaformCoin?
)