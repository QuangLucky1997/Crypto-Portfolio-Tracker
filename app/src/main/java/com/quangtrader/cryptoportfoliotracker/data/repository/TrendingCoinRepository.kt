package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.CoinGeckoTrendingApi
import com.quangtrader.cryptoportfoliotracker.data.remote.CoinWrapper
import javax.inject.Inject

class TrendingCoinRepository @Inject constructor(private val trendingApi: CoinGeckoTrendingApi) {
    suspend fun getTrending(): List<CoinWrapper> {
        return trendingApi.getListTrending().coins
    }

}