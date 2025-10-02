package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.NewsApi
import com.quangtrader.cryptoportfoliotracker.data.remote.NewsResponse
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsApi: NewsApi) {
    suspend fun getNewsByType(types: String): List<NewsResponse> {
        return newsApi.getNewsByType(types)
    }
}