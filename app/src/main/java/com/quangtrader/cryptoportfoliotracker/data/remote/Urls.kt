package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class Urls(
    @SerializedName("website") val website: List<String>?,
    @SerializedName("twitter") val twitter: List<String>?,
    @SerializedName("message_board") val messageBoard: List<String>?,
    @SerializedName("chat") val chat: List<String>?,
    @SerializedName("facebook") val facebook: List<String>?,
    @SerializedName("explorer") val explorer: List<String>?,
    @SerializedName("reddit") val reddit: List<String>?,
    @SerializedName("technical_doc") val technicalDoc: List<String>?,
    @SerializedName("source_code") val sourceCode: List<String>?,
    @SerializedName("announcement") val announcement: List<String>?
)