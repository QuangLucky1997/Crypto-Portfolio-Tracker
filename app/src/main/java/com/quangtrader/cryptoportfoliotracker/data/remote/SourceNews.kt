package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SourceNews(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String
)
