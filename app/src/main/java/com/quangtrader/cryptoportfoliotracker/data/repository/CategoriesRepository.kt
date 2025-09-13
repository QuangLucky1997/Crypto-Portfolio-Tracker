package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.CoinGeckoTrendingApi
import com.quangtrader.cryptoportfoliotracker.data.remote.ResponseCategoryCoin
import javax.inject.Inject

class CategoriesRepository @Inject constructor(private val categoriesApi: CoinGeckoTrendingApi) {
    suspend fun getCategories(): List<ResponseCategoryCoin> {
        return categoriesApi.getCategories()
    }

}