package com.quangtrader.cryptoportfoliotracker.data.api

import com.quangtrader.cryptoportfoliotracker.data.remote.NewsResponse
import com.quangtrader.cryptoportfoliotracker.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface NewsApi {
    @Headers("X-API-KEY: ${Constants.API_NEWS}")
    @GET("/news/type/{typeNews}")
    suspend fun getNewsByType(
        @Path("typeNews") typeNews: String,
    ): List<NewsResponse>
}