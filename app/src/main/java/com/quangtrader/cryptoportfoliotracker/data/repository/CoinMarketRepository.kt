package com.quangtrader.cryptoportfoliotracker.data.repository

import com.quangtrader.cryptoportfoliotracker.data.api.CoinGeckoTrendingApi
import com.quangtrader.cryptoportfoliotracker.data.remote.GlobalResponse
import javax.inject.Inject

class CoinMarketRepository @Inject constructor(private val coinGeckoTrendingApi: CoinGeckoTrendingApi) {
    suspend fun getMarketCap(): GlobalResponse {
        return coinGeckoTrendingApi.getMarketCap()
    }
}