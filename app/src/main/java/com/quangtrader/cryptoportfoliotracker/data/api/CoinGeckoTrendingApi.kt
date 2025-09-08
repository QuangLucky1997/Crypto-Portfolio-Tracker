package com.quangtrader.cryptoportfoliotracker.data.api

import com.quangtrader.cryptoportfoliotracker.data.remote.TrendingResponse
import retrofit2.http.GET

interface CoinGeckoTrendingApi {
    @GET("/api/v3/search/trending")
    suspend fun getListTrending(
    ): TrendingResponse
}