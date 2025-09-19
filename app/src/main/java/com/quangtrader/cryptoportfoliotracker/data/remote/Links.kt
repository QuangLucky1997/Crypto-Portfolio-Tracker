package com.quangtrader.cryptoportfoliotracker.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class Links(
    @SerializedName("homepage")
    val homepage: List<String>,
    @SerializedName("whitepaper")
    val whitepaper: String,
    @SerializedName("blockchain_site")
    val blockchainSite: List<String>,
    @SerializedName("official_forum_url")
    val officialForumUrl: List<Any?>,
    @SerializedName("chat_url")
    val chatUrl: List<String>,
    @SerializedName("announcement_url")
    val announcementUrl: List<Any?>,
    @SerializedName("snapshot_url")
    val snapshotUrl: Any?,
    @SerializedName("twitter_screen_name")
    val twitterScreenName: String,
    @SerializedName("facebook_username")
    val facebookUsername: String,
    @SerializedName("bitcointalk_thread_identifier")
    val bitcointalkThreadIdentifier: Any?,
    @SerializedName("telegram_channel_identifier")
    val telegramChannelIdentifier: String,
    @SerializedName("subreddit_url")
    val subredditUrl: Any?,
)