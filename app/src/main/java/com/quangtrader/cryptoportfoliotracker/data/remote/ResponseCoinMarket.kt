package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.quangtrader.cryptoportfoliotracker.data.remote.Data
data class ResponseCoinMarket(
    @SerializedName("status") var status: Status? = Status(),
    @SerializedName("data") var data: ArrayList<Data> = arrayListOf()
)