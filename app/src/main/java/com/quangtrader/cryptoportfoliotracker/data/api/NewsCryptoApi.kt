package com.quangtrader.cryptoportfoliotracker.data.api

import com.quangtrader.cryptoportfoliotracker.data.remote.NewsByTokenResponse
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsCryptoApi {
    @GET("everything")
    suspend fun getNewsByToken(
        @Query("q") tokenNews: String,
        @Query("apiKey") apiKey: String = Constants.NEWS_API
    ): NewsByTokenResponse
}