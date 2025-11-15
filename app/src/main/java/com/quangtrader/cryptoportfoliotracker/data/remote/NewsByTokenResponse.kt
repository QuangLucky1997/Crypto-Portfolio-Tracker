package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NewsByTokenResponse(
    @SerializedName("status") val status: String = "",
    @SerializedName("totalResults") val totalResults: Long = 0L,
    @SerializedName("articles") val articles: List<Article> = emptyList()
)